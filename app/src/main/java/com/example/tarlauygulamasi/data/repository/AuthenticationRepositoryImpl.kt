package com.example.tarlauygulamasi.data.repository

import android.util.Log
import com.example.tarlauygulamasi.data.model.UserDto
import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth,
    private val db: FirebaseFirestore
): AuthenticationRepository {

    override fun register(username:String, email:String, password: String, callback:(Resource<Boolean>) -> Unit )   {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = UserDto(username, email)

                    db.collection("Users").document(uid).set(user)
                        .addOnSuccessListener {
                            callback(Resource.Success(true))
                        }
                        .addOnFailureListener {
                            callback(Resource.Error("Firestore hatası"))
                        }

                } else {
                    Log.w("register", "Hesap oluşturulamadı", task.exception)
                    callback(Resource.Error(task.exception?.localizedMessage ?: "Yetkilendirme sağlanamadı"))
                }
            }



    }


    override fun login(email: String, password: String, callback: (Resource<Boolean>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d("login", "Giriş başarılı")
                    val user = auth.currentUser
                    callback(Resource.Success(true))
                } else {
                    Log.w("login", "Yanlış e-posta veya şifre", task.exception)
                    callback(Resource.Error(task.exception?.localizedMessage ?: "Yanlış e-posta veya şifre"))
                }
            }
    }


    override fun logout() {

        auth.signOut()

    }


}
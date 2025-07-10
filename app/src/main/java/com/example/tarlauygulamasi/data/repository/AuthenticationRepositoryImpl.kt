package com.example.tarlauygulamasi.data.repository

import android.util.Log
import com.example.tarlauygulamasi.data.entity.User
import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.domain.repository.UserRepository
import com.example.tarlauygulamasi.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth,
    private val userRepository: UserRepository
): AuthenticationRepository {

    override fun register(username:String, email:String, password: String, callback:(Resource<Boolean>) -> Unit )   {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(uid, username, email)


                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            userRepository.insertUser(user)
                            callback(Resource.Success(true))
                        } catch (e: Exception) {
                            callback(Resource.Error("Veritabanına eklenemedi: ${e.localizedMessage}"))
                        }
                    }


                    //Kaydet

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
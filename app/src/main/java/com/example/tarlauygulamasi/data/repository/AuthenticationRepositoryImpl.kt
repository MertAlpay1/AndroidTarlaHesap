package com.example.tarlauygulamasi.data.repository

import android.util.Log
import com.example.tarlauygulamasi.data.locale.entity.User
import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.domain.repository.UserRepository
import com.example.tarlauygulamasi.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth,
    private val userRepository: UserRepository
): AuthenticationRepository {

    override fun register(username:String, email:String, password: String): Flow<Resource<Boolean>> = flow  {
        try {
            val result=auth.createUserWithEmailAndPassword(email,password)

            val uid = auth.currentUser?.uid ?: throw Exception("Kullanıcı kimliği oluşturulamadı")
            val user = User(uid, username, email)

            userRepository.insertUser(user)
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "Yetkilendirme sağlanamadı"))
        }

    }.flowOn(Dispatchers.IO) //Veri tabanı için


    override fun login(email: String, password: String) : Flow<Resource<Boolean>> = flow {
        try{
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(true))

        }catch (e: Exception){
            emit(Resource.Error(e.localizedMessage?:"Giriş Yapılamadı"))
        }
    }


    override fun logout() {

        auth.signOut()
        Log.d("Giriş", "Kullanıcı çıkış yaptı. currentUser: ${auth.currentUser}")

    }


}
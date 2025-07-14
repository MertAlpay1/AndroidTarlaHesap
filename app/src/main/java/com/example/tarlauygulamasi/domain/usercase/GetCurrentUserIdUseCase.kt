package com.example.tarlauygulamasi.domain.usercase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val auth:FirebaseAuth
) {

    operator fun invoke():String{
        val user=auth.currentUser

        Log.d("kullanıc1", user?.uid ?: "Kullnıcı bulunamadı")
        return user?.uid ?: ""
    }


}
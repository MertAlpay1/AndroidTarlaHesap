package com.example.tarlauygulamasi.domain.usercase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val auth:FirebaseAuth
) {

    operator fun invoke():String{
        val user= auth.currentUser

        return user!!.uid
    }


}
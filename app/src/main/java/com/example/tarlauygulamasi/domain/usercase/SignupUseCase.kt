package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.util.resource.Resource
import javax.inject.Inject

class SignupUseCase @Inject constructor(

    private val repository: AuthenticationRepository
){

    operator fun invoke(username: String, email: String, password: String,callback: (Resource<Boolean>) -> Unit){
        repository.register(username,email,password,callback)
    }

}
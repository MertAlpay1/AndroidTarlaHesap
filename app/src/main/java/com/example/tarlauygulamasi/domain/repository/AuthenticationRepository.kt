package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.util.resource.Resource

interface AuthenticationRepository {

    fun register(username:String,email:String,password: String,callback:(Resource<Boolean>) -> Unit )

    fun login(email: String, password: String,callback: (Resource<Boolean>) -> Unit)

    fun logout()
}
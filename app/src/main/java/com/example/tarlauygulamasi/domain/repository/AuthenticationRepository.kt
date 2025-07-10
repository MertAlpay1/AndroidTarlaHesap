package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.util.resource.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun register(username:String,email:String,password: String): Flow<Resource<Boolean>>

    fun login(email: String, password: String): Flow<Resource<Boolean>>

    fun logout()
}
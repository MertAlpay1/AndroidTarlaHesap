package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.util.resource.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignupUseCase @Inject constructor(

    private val repository: AuthenticationRepository
){

    operator fun invoke(username: String, email: String, password:String): Flow<Resource<Boolean>> {
        return repository.register(username,email,password)
    }

}
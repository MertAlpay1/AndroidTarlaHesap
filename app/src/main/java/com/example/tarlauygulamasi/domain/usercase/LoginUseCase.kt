package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import com.example.tarlauygulamasi.util.resource.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(

    private val repository: AuthenticationRepository

) {
    operator fun invoke(email: String, password: String, callback: (Resource<Boolean>) -> Unit) {
        repository.login(email, password, callback)
    }

}
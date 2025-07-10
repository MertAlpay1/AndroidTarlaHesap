package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.domain.repository.AuthenticationRepository
import javax.inject.Inject

class SignoutUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(){
        repository.logout()
    }


}
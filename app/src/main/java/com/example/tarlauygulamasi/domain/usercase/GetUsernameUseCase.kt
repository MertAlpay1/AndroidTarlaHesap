package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.database.UserDatabase
import com.example.tarlauygulamasi.data.locale.entity.User
import com.example.tarlauygulamasi.domain.repository.UserRepository
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(

    private val userRepository: UserRepository
)  {

     suspend operator fun invoke(id:String): User? {
        return userRepository.getUserById(id)
    }


}
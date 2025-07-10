package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.data.entity.User

interface UserRepository {

    suspend fun insertUser(user: User)
    suspend fun getUserById(id: String): User?

}
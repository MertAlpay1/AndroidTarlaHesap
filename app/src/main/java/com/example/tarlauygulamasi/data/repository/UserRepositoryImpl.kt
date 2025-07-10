package com.example.tarlauygulamasi.data.repository

import com.example.tarlauygulamasi.data.dao.UserDao
import com.example.tarlauygulamasi.data.entity.User
import com.example.tarlauygulamasi.domain.repository.UserRepository

class UserRepositoryImpl(

    private val userDao: UserDao

) : UserRepository {



    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)
    }
}
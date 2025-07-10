package com.example.tarlauygulamasi.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tarlauygulamasi.data.entity.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user : User)

    @Query("SELECT * FROM user WHERE id=:userId ")
    fun getUserById(userId: String):User?

}
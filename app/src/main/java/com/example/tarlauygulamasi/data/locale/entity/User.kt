package com.example.tarlauygulamasi.data.locale.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (

    @PrimaryKey(autoGenerate = false)
    val id:String ,
    val username: String = "",
    val email: String = "",


)
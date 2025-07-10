package com.example.tarlauygulamasi.data.mapper

import com.example.tarlauygulamasi.data.model.UserDto
import com.example.tarlauygulamasi.domain.model.User

class UserMapper {

    fun UserDto.toDomain(): User {
        return User(username, email)
    }

    fun User.toDto(): UserDto {
        return UserDto(username, email)
    }

}
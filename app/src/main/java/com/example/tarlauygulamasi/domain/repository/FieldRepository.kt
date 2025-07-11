package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.data.locale.entity.Field

interface FieldRepository {

    suspend fun insertField(field: Field)

    suspend fun deleteField(field: Field)
}
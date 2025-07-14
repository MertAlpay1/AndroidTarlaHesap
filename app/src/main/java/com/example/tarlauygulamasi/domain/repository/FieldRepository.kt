package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.data.locale.entity.Field
import kotlinx.coroutines.flow.Flow

interface FieldRepository {

    suspend fun insertField(field: Field)

    suspend fun deleteField(field: Field)

    fun getFieldByUserId(userId: String): Flow<List<Field>>

}
package com.example.tarlauygulamasi.domain.repository

import com.example.tarlauygulamasi.data.locale.entity.Field
import kotlinx.coroutines.flow.Flow

interface FieldRepository {

    suspend fun insertField(field: Field)

    suspend fun deleteField(fieldId:Long)

    fun getFieldByUserId(userId: String): Flow<List<Field>>

    suspend fun getFieldByFieldID(fieldId:Long): Field

    suspend fun updateField(field: Field)

    fun getAllField(): Flow<List<Field>>

}
package com.example.tarlauygulamasi.data.repository

import com.example.tarlauygulamasi.data.locale.dao.FieldDao
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository

class FieldRepositoryImpl(
    private val fieldDao: FieldDao

): FieldRepository{
    override suspend fun insertField(field: Field) {

    }

    override suspend fun deleteField(field: Field) {
    }

}
package com.example.tarlauygulamasi.data.repository

import com.example.tarlauygulamasi.data.locale.dao.FieldDao
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import kotlinx.coroutines.flow.Flow

class FieldRepositoryImpl(
    private val fieldDao: FieldDao

): FieldRepository{
    override suspend fun insertField(field: Field) {

        fieldDao.insertField(field)
    }

    override suspend fun deleteField(field: Field) {
        fieldDao.deleteField(field)
    }

    override  fun getFieldByUserId(userId: String): Flow<List<Field>> {
        return fieldDao.getFieldByUserId(userId)
    }


}
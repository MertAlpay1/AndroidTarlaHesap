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

    override suspend fun deleteField(fieldId:Long) {
        fieldDao.deleteFieldBy(fieldId)
    }

    override  fun getFieldByUserId(userId: String): Flow<List<Field>> {
        return fieldDao.getFieldByUserId(userId)
    }

    override suspend fun getFieldByFieldID(fieldId: Long): Field {
        return fieldDao.getFieldByFieldId(fieldId)
    }

    override suspend fun updateField(field: Field) {
        fieldDao.updateField(field)
    }


}
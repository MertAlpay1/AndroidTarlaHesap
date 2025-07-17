package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import javax.inject.Inject

class GetFieldUseCase@Inject constructor(
    private val repository: FieldRepository
) {

    suspend operator fun invoke(fieldId:Long): Field{
        return repository.getFieldByFieldID(fieldId)
    }

}
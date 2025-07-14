package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import javax.inject.Inject

class InsertFieldUseCase @Inject constructor(
    private val fieldRepository: FieldRepository
) {

    suspend operator fun invoke(field: Field){
        fieldRepository.insertField(field)
    }


}
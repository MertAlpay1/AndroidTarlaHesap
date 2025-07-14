package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.domain.repository.FieldRepository
import javax.inject.Inject

class DeleteFieldUseCase@Inject constructor(
    private val fieldRepository: FieldRepository
) {

    suspend operator fun invoke(fieldId:Long){

        fieldRepository.deleteField(fieldId)
    }

}
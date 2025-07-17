package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import javax.inject.Inject

class UpdateFieldUseCase@Inject constructor(
    private val repository: FieldRepository
) {

    suspend operator fun invoke(field: Field){
        repository.updateField(field)
    }

}
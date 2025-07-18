package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFieldUseCase@Inject constructor(
    private val repository: FieldRepository
) {

    operator fun invoke(): Flow<List<Field>> {

        return repository.getAllField()
    }

}
package com.example.tarlauygulamasi.domain.usercase

import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFieldUseCase @Inject constructor(

    private val fieldRepository: FieldRepository
){


     operator fun invoke(userId:String): Flow<List<Field>>{
       return fieldRepository.getFieldByUserId(userId)
    }

}
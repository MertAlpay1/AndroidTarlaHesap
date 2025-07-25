package com.example.tarlauygulamasi.ui.field_detail

import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import com.example.tarlauygulamasi.domain.usercase.GetFieldUseCase
import com.example.tarlauygulamasi.domain.usercase.GetUserFieldUseCase
import com.example.tarlauygulamasi.domain.usercase.UpdateFieldUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FieldDetailViewModel @Inject constructor(

    private val getFieldUseCase: GetFieldUseCase,
    private val updateFieldUseCase: UpdateFieldUseCase,

): ViewModel() {

    suspend fun getField(fieldId:Long): Field{
        return getFieldUseCase(fieldId)
    }
    suspend fun updateField(field: Field){
        updateFieldUseCase(field)
    }
}
package com.example.tarlauygulamasi.ui.newfield

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.data.locale.entity.User
import com.example.tarlauygulamasi.domain.repository.FieldRepository
import com.example.tarlauygulamasi.domain.usercase.GetAllFieldUseCase
import com.example.tarlauygulamasi.domain.usercase.GetCurrentUserIdUseCase
import com.example.tarlauygulamasi.domain.usercase.InsertFieldUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CreateNewFieldViewModel @Inject constructor(
    private val insertFieldUseCase: InsertFieldUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getAllFieldUseCase: GetAllFieldUseCase,
) : ViewModel() {


    suspend fun getCurrentUserId(): String{

        return getCurrentUserIdUseCase()

    }

    suspend fun insertField(name:String,area: Double,points:MutableList<LatLng>){

        val id=getCurrentUserId()

        val field:Field= Field( 0,name,area,points,id)

        Log.d("Kullanıcı",id)
        insertFieldUseCase(field)


    }

    fun getAllField(): Flow<List<Field>> {
        return getAllFieldUseCase()
    }


}
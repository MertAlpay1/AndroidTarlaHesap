package com.example.tarlauygulamasi.ui.newfield2

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.domain.usercase.GetCurrentUserIdUseCase
import com.example.tarlauygulamasi.domain.usercase.InsertFieldUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewFieldByWalkingViewModel @Inject constructor(
    private val insertFieldUseCase: InsertFieldUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
): ViewModel() {


    suspend fun getCurrentUserId(): String{

        return getCurrentUserIdUseCase()

    }

    suspend fun insertField(name:String,area: Double,points:MutableList<LatLng>){

        val id=getCurrentUserId()

        val field:Field= Field( 0,name,area,points,id)

        Log.d("Kullanıcı",id)
        insertFieldUseCase(field)


    }


}
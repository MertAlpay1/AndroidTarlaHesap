package com.example.tarlauygulamasi.ui.home

import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.data.locale.entity.Field
import com.example.tarlauygulamasi.data.locale.entity.User
import com.example.tarlauygulamasi.domain.usercase.DeleteFieldUseCase
import com.example.tarlauygulamasi.domain.usercase.GetCurrentUserIdUseCase
import com.example.tarlauygulamasi.domain.usercase.GetUserFieldUseCase
import com.example.tarlauygulamasi.domain.usercase.GetUsernameUseCase
import com.example.tarlauygulamasi.domain.usercase.SignoutUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val signoutUseCase: SignoutUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserFieldUseCase: GetUserFieldUseCase,
    private val deleteFieldUseCase: DeleteFieldUseCase,
) : ViewModel(){

     suspend fun getUsername(): User?{

        val id=getCurrentUserIdUseCase()

        return getUsernameUseCase(id)
    }

      fun  getUserField(): Flow<List<Field>>{

          val id=getCurrentUserIdUseCase()

        return  getUserFieldUseCase(id)
    }

    suspend fun deleteField(fieldId:Long){
        deleteFieldUseCase(fieldId)
    }
    fun signout(){
        signoutUseCase()
    }


}
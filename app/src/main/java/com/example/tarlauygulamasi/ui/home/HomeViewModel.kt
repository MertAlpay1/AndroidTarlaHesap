package com.example.tarlauygulamasi.ui.home

import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.data.locale.entity.User
import com.example.tarlauygulamasi.domain.usercase.GetCurrentUserIdUseCase
import com.example.tarlauygulamasi.domain.usercase.GetUsernameUseCase
import com.example.tarlauygulamasi.domain.usercase.SignoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val signoutUseCase: SignoutUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,

) : ViewModel(){

     suspend fun getUsername(): User?{

        val id=getCurrentUserIdUseCase()

        return getUsernameUseCase(id)
    }




    fun signout(){
        signoutUseCase()
    }


}
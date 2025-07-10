package com.example.tarlauygulamasi.ui.home

import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.domain.usercase.SignoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val signoutUseCase: SignoutUseCase

) : ViewModel(){


    fun signout(){
        signoutUseCase()
    }


}
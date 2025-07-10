package com.example.tarlauygulamasi.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.domain.usercase.SignupUseCase
import com.example.tarlauygulamasi.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(

    private val SignupUseCaseImpl: SignupUseCase

) : ViewModel() {

    val signupEmailInput = MutableLiveData<String>()
    val signupPasswordInput = MutableLiveData<String>()
    val signupUsernameInput = MutableLiveData<String>()
    val signupConfirmPasswordInput = MutableLiveData<String>()

    private val _registerResult = MutableLiveData<Resource<Boolean>>()
    val registerResult: LiveData<Resource<Boolean>> = _registerResult


    fun register(username: String, email: String, password: String) {
        _registerResult.value = Resource.Loading()
        SignupUseCaseImpl(username, email, password) { resource ->
            _registerResult.postValue(resource)
        }
    }


}
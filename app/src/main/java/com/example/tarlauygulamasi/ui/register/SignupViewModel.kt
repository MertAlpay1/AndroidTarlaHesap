package com.example.tarlauygulamasi.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarlauygulamasi.domain.usercase.SignupUseCase
import com.example.tarlauygulamasi.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(

    private val SignupUseCaseImpl: SignupUseCase

) : ViewModel() {

    val signupEmailInput = MutableStateFlow<String>("")
    val signupPasswordInput = MutableStateFlow<String>("")
    val signupUsernameInput = MutableStateFlow<String>("")
    val signupConfirmPasswordInput = MutableStateFlow<String>("")

    private val _registerResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val registerResult: MutableStateFlow<Resource<Boolean>> = _registerResult

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            SignupUseCaseImpl(username,email,password).collect { resource ->
                registerResult.value=resource
            }
        }
    }


}
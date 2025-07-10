package com.example.tarlauygulamasi.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tarlauygulamasi.domain.usercase.LoginUseCase
import com.example.tarlauygulamasi.util.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCaseImpl: LoginUseCase
) : ViewModel() {

    val loginEmailInput= MutableLiveData<String>()
    val loginPasswordInput= MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Resource<Boolean>>()
    val loginResult: LiveData<Resource<Boolean>> = _loginResult

    //flaw
    fun login(email: String, password: String) {
        _loginResult.value = Resource.Loading()
        loginUseCaseImpl(email, password) { resource ->
            _loginResult.postValue(resource)
        }
    }


}
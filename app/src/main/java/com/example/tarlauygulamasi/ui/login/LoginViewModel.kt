package com.example.tarlauygulamasi.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tarlauygulamasi.domain.usercase.GetCurrentUserIdUseCase
import com.example.tarlauygulamasi.domain.usercase.LoginUseCase
import com.example.tarlauygulamasi.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCaseImpl: LoginUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val auth:FirebaseAuth,
) : ViewModel() {

    val loginEmailInput= MutableStateFlow("")
    val loginPasswordInput= MutableStateFlow("")

    private val _loginResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val loginResult: MutableStateFlow<Resource<Boolean>> = _loginResult

    fun login(email: String, password: String) {
         viewModelScope.launch {
             loginUseCaseImpl(email,password).collect { resource ->
                 loginResult.value=resource
             }
         }
    }

    fun checkAuth(): Boolean{
        val user=auth.currentUser
        return user != null
    }







}
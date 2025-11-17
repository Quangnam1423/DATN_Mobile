package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.LoginCredentials
import com.example.datn_mobile.domain.repository.AuthRepository
import com.example.datn_mobile.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onLoginClicked(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)

            val credentials = LoginCredentials(email, password)

            // call LoginUseCase to handle login logic
            when (val result = loginUseCase(credentials)) {
                is Resource.Success -> {
                    _loginState.value = LoginState(isLoginSuccess = true)
                }
                is Resource.Error -> {
                    _loginState.value = LoginState(errorMessage = result.message)
                }
                else -> { }
            }
        }
    }
}



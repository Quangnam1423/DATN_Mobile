package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogoutState(
    val isLoading: Boolean = false,
    val isLogoutSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _logoutState = MutableStateFlow(LogoutState())
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutState(isLoading = true)

            when (val result = logoutUseCase()) {
                is Resource.Success -> {
                    _logoutState.value = LogoutState(isLogoutSuccess = true)
                }
                is Resource.Error -> {
                    _logoutState.value = LogoutState(errorMessage = result.message)
                }
                else -> {
                    // Handle Loading state if needed
                }
            }
        }
    }

    fun resetState() {
        _logoutState.value = LogoutState()
    }
}


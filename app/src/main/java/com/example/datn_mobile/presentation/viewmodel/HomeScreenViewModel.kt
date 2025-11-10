package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Role
import com.example.datn_mobile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Home Data State
 */
data class HomeState(
    val isLoading: Boolean = true,
    val userRole: Role = Role.UNKNOWN
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init{
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _homeState.value = HomeState(isLoading = true)

            val result = getUserProfileUseCase()

            if (result is Resource.Success) {
                _homeState.value = HomeState(
                    isLoading = false
                )
            } else {
                _homeState.value = HomeState(
                    isLoading = false,
                    userRole = Role.UNKNOWN
                )
            }
        }
    }
}
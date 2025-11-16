package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.local.PreferenceDataSource
import com.example.datn_mobile.data.network.api.UserApiService
import com.example.datn_mobile.data.network.dto.toUserProfile
import com.example.datn_mobile.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val hasToken: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    init {
        checkAuthentication()
    }

    /**
     * Kiểm tra xem user có đăng nhập hay không
     */
    private fun checkAuthentication() {
        viewModelScope.launch {
            preferenceDataSource.tokenFlow.collect { token ->
                val hasToken = !token.isNullOrBlank()
                _profileState.value = _profileState.value.copy(hasToken = hasToken)

                if (hasToken) {
                    _profileState.value = _profileState.value.copy(isAuthenticated = true)
                    loadUserProfile()
                } else {
                    _profileState.value = _profileState.value.copy(
                        isAuthenticated = false,
                        userProfile = null,
                        error = null
                    )
                }
            }
        }
    }

    /**
     * Lấy thông tin profile của user từ API
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true, error = null)

            try {
                val response = userApiService.getUserProfile()

                if (response.isSuccessful) {
                    val profileResponse = response.body()

                    if (profileResponse != null && profileResponse.result != null) {
                        // Convert ProfileResponseData to UserProfile
                        val userProfile = profileResponse.result.toUserProfile()
                        _profileState.value = _profileState.value.copy(
                            userProfile = userProfile,
                            isLoading = false,
                            error = null  // ✅ Ensure error is cleared on success
                        )
                    } else {
                        _profileState.value = _profileState.value.copy(
                            isLoading = false,
                            error = "Không có dữ liệu profile",
                            userProfile = null
                        )
                    }
                } else {
                    // Xử lý các lỗi không phải 200 (401, 403, 500, ...)
                    // Nếu là lỗi xác thực, xóa token
                    when (response.code()) {
                        401, 403 -> {
                            // Unauthorized - Token hết hạn hoặc không hợp lệ
                            preferenceDataSource.clearToken()
                            _profileState.value = ProfileState(isAuthenticated = false)
                        }
                        500 -> {
                            // Internal Server Error - có thể token invalid hoặc có lỗi server
                            // Thử xóa token để user login lại
                            preferenceDataSource.clearToken()
                            _profileState.value = ProfileState(isAuthenticated = false)
                        }
                        else -> {
                            // Lỗi khác
                            _profileState.value = _profileState.value.copy(
                                isLoading = false,
                                error = "Lỗi: ${response.code()} - ${response.message()}"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = "Lỗi kết nối: ${e.message ?: "Vui lòng thử lại"}"
                )
            }
        }
    }

    /**
     * Refresh profile data
     */
    fun refreshProfile() {
        loadUserProfile()
    }

    /**
     * Logout
     */
    fun logout() {
        viewModelScope.launch {
            preferenceDataSource.clearToken()
            _profileState.value = ProfileState(isAuthenticated = false)
        }
    }
}


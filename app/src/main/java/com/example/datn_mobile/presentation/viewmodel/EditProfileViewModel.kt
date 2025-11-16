package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.local.PreferenceDataSource
import com.example.datn_mobile.data.network.api.UserApiService
import com.example.datn_mobile.data.network.dto.UserUpdateRequest
import com.example.datn_mobile.data.network.dto.toUserProfile
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val hasToken: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {

    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState = _editProfileState.asStateFlow()

    init {
        checkAuthenticationAndLoadProfile()
    }

    /**
     * Kiểm tra xem user có đăng nhập và load profile
     */
    private fun checkAuthenticationAndLoadProfile() {
        viewModelScope.launch {
            preferenceDataSource.tokenFlow.collect { token ->
                val hasToken = !token.isNullOrBlank()
                _editProfileState.value = _editProfileState.value.copy(hasToken = hasToken)

                if (hasToken) {
                    _editProfileState.value = _editProfileState.value.copy(isAuthenticated = true)
                    loadUserProfile()
                } else {
                    _editProfileState.value = _editProfileState.value.copy(
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
     * Nếu gặp lỗi, vẫn để user edit với form trống
     * Chỉ show error khi thực sự cần (ví dụ: khi gọi API PUT)
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _editProfileState.value = _editProfileState.value.copy(isLoading = true, error = null)

            try {
                val response = userApiService.getUserProfile()

                if (response.isSuccessful) {
                    val profileResponse = response.body()

                    if (profileResponse != null && profileResponse.result != null) {
                        val userProfile = profileResponse.result.toUserProfile()
                        _editProfileState.value = _editProfileState.value.copy(
                            userProfile = userProfile,
                            isLoading = false,
                            error = null
                        )
                    } else {
                        // No data but success - allow user to edit with empty form
                        _editProfileState.value = _editProfileState.value.copy(
                            userProfile = null,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    // ✅ CHANGED: Không hiển thị error, cho phép user edit với form trống
                    // Error sẽ được show nếu user click save và API PUT fail
                    when (response.code()) {
                        401, 403 -> {
                            // Token issue - but allow editing, error will show on save
                            _editProfileState.value = _editProfileState.value.copy(
                                userProfile = null,
                                isLoading = false,
                                error = null  // ✅ Don't show error
                            )
                        }
                        500 -> {
                            // Server error - allow editing, error will show on save
                            _editProfileState.value = _editProfileState.value.copy(
                                userProfile = null,
                                isLoading = false,
                                error = null  // ✅ Don't show error
                            )
                        }
                        else -> {
                            // Other errors - allow editing
                            _editProfileState.value = _editProfileState.value.copy(
                                userProfile = null,
                                isLoading = false,
                                error = null  // ✅ Don't show error
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle JSON deserialization or network errors
                // Don't show error - let user edit with empty form
                _editProfileState.value = _editProfileState.value.copy(
                    userProfile = null,
                    isLoading = false,
                    error = null  // Don't block - let user edit anyway
                )
            }
        }
    }

    fun updateUserProfile(
        fullName: String?,
        address: String?,
        dob: String?,
        password: String?
    ) {
        viewModelScope.launch {
            _editProfileState.value = _editProfileState.value.copy(
                isSaving = true,
                error = null
            )

            try {
                val updateRequest = UserUpdateRequest(
                    fullName = fullName?.takeIf { it.isNotBlank() },
                    address = address?.takeIf { it.isNotBlank() },
                    dob = dob?.takeIf { it.isNotBlank() },
                    password = password?.takeIf { it.isNotBlank() }
                )

                val response = userApiService.updateUserProfile(updateRequest)

                if (response.isSuccessful) {
                    try {
                        val updateResponse = response.body()

                        if (updateResponse != null && updateResponse.result != null) {
                            val updatedProfile = updateResponse.result.toUserProfile()
                            _editProfileState.value = _editProfileState.value.copy(
                                userProfile = updatedProfile,
                                isSaving = false,
                                error = null
                            )
                            // ✅ Use MessageManager instead of state
                            MessageManager.showSuccess("✅ Cập nhật profile thành công")
                        } else {
                            _editProfileState.value = _editProfileState.value.copy(
                                isSaving = false,
                                error = "❌ Lỗi: Không có dữ liệu trả về từ server"
                            )
                            MessageManager.showError("Lỗi: Không có dữ liệu trả về từ server")
                        }
                    } catch (jsonError: com.squareup.moshi.JsonDataException) {
                        // Backend trả về 200 nhưng có vấn đề với deserialization
                        // Vẫn coi là thành công vì HTTP 200
                        _editProfileState.value = _editProfileState.value.copy(
                            isSaving = false,
                            error = null
                        )
                        MessageManager.showSuccess("✅ Cập nhật profile thành công")
                    }
                } else {
                    // ✅ Show lỗi khi API PUT fail
                    when (response.code()) {
                        400 -> {
                            _editProfileState.value = _editProfileState.value.copy(
                                isSaving = false,
                                error = "Dữ liệu không hợp lệ"
                            )
                            MessageManager.showError("Dữ liệu không hợp lệ. Kiểm tra ngày sinh (YYYY-MM-DD) và mật khẩu (≥6 ký tự)")
                        }
                        401, 403 -> {
                            _editProfileState.value = _editProfileState.value.copy(
                                isSaving = false,
                                error = "Phiên hết hạn"
                            )
                            MessageManager.showError("Phiên đăng nhập hết hạn")
                        }
                        500 -> {
                            _editProfileState.value = _editProfileState.value.copy(
                                isSaving = false,
                                error = "Lỗi máy chủ"
                            )
                            MessageManager.showError("Lỗi máy chủ. Vui lòng thử lại sau")
                        }
                        else -> {
                            _editProfileState.value = _editProfileState.value.copy(
                                isSaving = false,
                                error = "Lỗi: ${response.code()}"
                            )
                            MessageManager.showError("Lỗi: ${response.code()} - ${response.message()}")
                        }
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e is com.squareup.moshi.JsonDataException -> {
                        "Lỗi: Dữ liệu không hợp lệ"
                    }
                    e.message?.contains("timeout", ignoreCase = true) == true -> {
                        "Timeout: Mất quá lâu để lưu"
                    }
                    else -> {
                        "Lỗi kết nối: ${e.message ?: "Vui lòng thử lại"}"
                    }
                }

                _editProfileState.value = _editProfileState.value.copy(
                    isSaving = false,
                    error = errorMessage
                )
                MessageManager.showError(errorMessage)
            }
        }
    }

    /**
     * Refresh profile data
     */
    fun refreshProfile() {
        loadUserProfile()
    }
}


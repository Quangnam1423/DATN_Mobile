package com.example.datn_mobile.data.repository

import com.example.datn_mobile.data.network.api.AuthApiService
import com.example.datn_mobile.data.network.dto.LoginRequest as LoginRequestDto
import com.example.datn_mobile.data.network.dto.RegisterRequest as RegisterRequestDto
import com.example.datn_mobile.data.network.dto.RegisterResponse
import com.example.datn_mobile.data.network.dto.ErrorResponse
import com.example.datn_mobile.data.network.dto.toUserProfile
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.RegisterCredentials
import com.example.datn_mobile.domain.model.UserCredentials
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.domain.repository.AuthRepository
import com.example.datn_mobile.domain.repository.UserPreferencesRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,
    private val prefsRepo: UserPreferencesRepository,
    private val moshi: Moshi
) : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): Resource<Unit> {
        return try {
            val response = authApi.login(LoginRequestDto(phoneNumber, password))
            val responseBody = response.body()
            if (response.isSuccessful && responseBody?.result != null) {
                val token = responseBody.result.token
                prefsRepo.saveAuthToken(token)
                prefsRepo.saveUserCredentials(UserCredentials(phoneNumber, password))
                Resource.Success(Unit)
            } else {
                // Parse error response
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        val adapter = moshi.adapter(ErrorResponse::class.java)
                        val errorResponse = adapter.fromJson(errorBody)
                        // Check if it's a user not found error
                        if (errorResponse != null) {
                            val message = errorResponse.message?.lowercase() ?: ""
                            // Check for user not found scenarios
                            if (message.contains("không tồn tại") || 
                                message.contains("not found") || 
                                message.contains("user not found") ||
                                message.contains("người dùng không tồn tại") ||
                                errorResponse.code == 1004 || // Common error code for user not found
                                response.code() == 404) {
                                "Người dùng không tồn tại"
                            } else {
                                // For other login errors (wrong password, wrong phone number, etc.)
                                "Số điện thoại hoặc mật khẩu đã sai"
                            }
                        } else {
                            "Số điện thoại hoặc mật khẩu đã sai"
                        }
                    } catch (e: Exception) {
                        "Số điện thoại hoặc mật khẩu đã sai"
                    }
                } else {
                    "Số điện thoại hoặc mật khẩu đã sai"
                }
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Đã xảy ra lỗi")
        }
    }

    override suspend fun register(credentials: RegisterCredentials): Resource<UserProfile> {
        return try {
            val registerRequestDto = RegisterRequestDto(
                fullName = credentials.fullName,
                email = credentials.email,
                phoneNumber = credentials.phoneNumber,
                password = credentials.password,
                role = credentials.role ?: "USER"
            )
            val response = authApi.register(registerRequestDto)
            if (response.isSuccessful) {
                val body = response.body()?.string()
                if (body != null) {
                    val adapter = moshi.adapter(RegisterResponse::class.java)
                    val registerResponse = adapter.fromJson(body)
                    if (registerResponse != null && registerResponse.code == 1000) {
                        val userProfile = registerResponse.toUserProfile()
                        Resource.Success(userProfile)
                    } else {
                        Resource.Error("Registration failed with code: ${registerResponse?.code}")
                    }
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error(response.message() ?: "Registration failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } catch (e: Exception) {
            // Ignore errors on logout
        }
        prefsRepo.clearAll()
    }

    override fun getAuthToken(): Flow<String?> {
        return prefsRepo.authToken
    }

    override suspend fun clearAuthToken() {
        prefsRepo.clearAll()
    }
}

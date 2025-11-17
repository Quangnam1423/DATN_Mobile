package com.example.datn_mobile.data.repository

import com.example.datn_mobile.data.local.PreferenceDataSource
import com.example.datn_mobile.data.network.api.AuthApiService
import com.example.datn_mobile.data.network.dto.LoginRequest
import com.example.datn_mobile.data.network.dto.RegisterRequest
import com.example.datn_mobile.data.network.dto.RegisterResponse
import com.example.datn_mobile.data.network.dto.toUserProfile
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.LoginCredentials
import com.example.datn_mobile.domain.model.RegisterCredentials
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val prefs: PreferenceDataSource
) : AuthRepository {

    override suspend fun login(credentials: LoginCredentials): Resource<Unit> {
        return try {
            val loginRequest = LoginRequest(
                phoneNumber = credentials.phoneNumber,
                password = credentials.password
            )

            val response = authApiService.login(loginRequest)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                val token = loginResponse?.result?.token

                if (token != null) {
                    prefs.saveToken(token)
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Login response body is empty or token is null")
                }
            } else {
                Resource.Error("Login failed: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Connection error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unknown error: ${e.message}")
        }
    }

    override suspend fun register(credentials: RegisterCredentials): Resource<UserProfile> {
        return try {
            val request = RegisterRequest(
                phoneNumber = credentials.phoneNumber,
                password = credentials.password
            )

            val response = authApiService.register(request)

            if (response.isSuccessful) {
                val registerResponse = response.body()

                if (registerResponse != null) {
                    Resource.Success(registerResponse.toUserProfile())
                } else {
                    Resource.Error("Register response body or result is null")
                }
            } else {
                Resource.Error("Register failed: ${response.message()}")
            }

        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Connection error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unknow error: ${e.message}")
        }
    }

    override suspend fun saveToken(token: String) {
        prefs.saveToken(token)
    }
}
//    override val tokenFlow: Flow<String?>
//        get() = prefs.tokenFlow
//
//    override val hasValidTokenFlow: Flow<Boolean>
//        get() = prefs.tokenFlow.map { !it.isNullOrBlank() }
//
//    override suspend fun logout() {
//        prefs.clearToken()
//    }
//
//    override suspend fun verifyToken(token: String): Boolean {
//        return try {
//            // 🔹 Giả lập gọi API kiểm tra token
//            // Thực tế bạn nên gọi Retrofit hoặc HttpClient ở đây
//            val isValid = token.startsWith("token_") && token.length > 10
//
//            // Nếu hợp lệ, giữ lại token
//            if (isValid) {
//                prefs.saveToken(token)
//            } else {
//                prefs.clearToken()
//            }
//
//            isValid
//        } catch (e: Exception) {
//            prefs.clearToken()
//            false
//        }
//    }
//
//    override suspend fun clearToken() {
//        prefs.clearToken()
//    }
// }

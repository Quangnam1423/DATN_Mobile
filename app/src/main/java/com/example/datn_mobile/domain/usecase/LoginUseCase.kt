package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.LoginCredentials
import com.example.datn_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor (
    private val authRepository: AuthRepository,
    //private val userRepository: UserRepository,
    //private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials) : Resource<Unit> {
        // validation
        if (credentials.email.isBlank() || credentials.password.isBlank()) {
            return Resource.Error("Username and password cannot be blank")
        }

        val loginResult = authRepository.login(credentials)

        if (loginResult is Resource.Success) {
            // userRepository.fetchAndSaveProfile()
            // notificationRepository.registerDeviceToken()
        }

        if (loginResult is Resource.Loading) {
            /*TO DO*/
        }
        return loginResult
    }
}
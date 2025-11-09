package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Role
import com.example.datn_mobile.domain.model.UserProfile
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    //private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<UserProfile> {
        //return userRepository.getUserProfile()
        return Resource.Success(UserProfile(
            email = "admin@gmail.com",
            id = "fake-user-id-123",
            fullName = "Người dùng Test",
            address = "123 Đường Test",
            dob = "2000-01-01",
            phoneNumber = "0987654321",
            roles = listOf(Role.USER) // Giả lập vai trò USER
        ))
    }
}
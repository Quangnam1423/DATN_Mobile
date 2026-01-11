package com.example.datn_mobile.domain.usecase

// import android.util.Patterns
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.RegisterCredentials
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
  private val authRepository: AuthRepository
) {

    suspend operator fun invoke(credentals: RegisterCredentials) : Resource<UserProfile> {
        if (credentals.fullName.isBlank()) {
            return Resource.Error("Họ và tên không được để trống")
        }

        if (credentals.email.isBlank()) {
            return Resource.Error("Email không được để trống")
        }

        if (credentals.phoneNumber.isBlank()) {
            return Resource.Error("Số điện thoại không được để trống")
        }

        if (credentals.phoneNumber.length < 10) {
            return Resource.Error("Số điện thoại không hợp lệ!")
        }

        if (credentals.password.length < 1) {
            return Resource.Error("Mật khẩu phải hợp lệ!")
        }

        return authRepository.register(credentals)
    }
}
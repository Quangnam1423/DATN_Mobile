package com.example.datn_mobile.domain.repository

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.LoginCredentials
import com.example.datn_mobile.domain.model.RegisterCredentials
import com.example.datn_mobile.domain.model.UserProfile

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Resource<Unit>

    suspend fun register(credentials: RegisterCredentials): Resource<UserProfile>
    suspend fun saveToken(token: String)
}
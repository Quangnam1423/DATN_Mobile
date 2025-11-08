package com.example.datn_mobile.domain.repository

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.LoginCredentials
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Resource<Unit>
    suspend fun saveToken(token: String)
}
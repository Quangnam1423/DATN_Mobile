package com.example.datn_mobile.data.network.api

import com.example.datn_mobile.data.network.dto.LoginRequest
import com.example.datn_mobile.data.network.dto.LoginResponse
import com.example.datn_mobile.data.network.interceptor.NoAuth
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Body

interface AuthApiService {
    @NoAuth
    @POST("/bej3/auth/log-in")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}
package com.example.datn_mobile.data.network.api

import com.example.datn_mobile.data.network.dto.ProfileResponse
import com.example.datn_mobile.data.network.dto.RegisterRequest
import com.example.datn_mobile.data.network.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("/bej3/user/profile")
    suspend fun getUserProfile(): Response<ProfileResponse>
}
package com.example.datn_mobile.data.network.dto

import com.squareup.moshi.Json

data class ProfileResponse(
    @field:Json(name="email") val email: String,
    @field:Json(name="password") val password: String,
    @field:Json(name="role") val role: String = "USER" // default value
)
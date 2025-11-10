package com.example.datn_mobile.domain.model

data class UserProfile(
    val id: String,
    val fullName: String?,    // user name could be null
    val address: String?,
    val dob: String?,
    val email: String,
    val phoneNumber: String?,
    val roles: List<Role>          // use Enum Role safe
)
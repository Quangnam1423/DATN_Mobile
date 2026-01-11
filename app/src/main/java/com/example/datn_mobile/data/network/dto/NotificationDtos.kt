package com.example.datn_mobile.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class RegisterFcmTokenRequest(
    val token: String,
    val deviceName: String,
    val osType: String
)

@JsonClass(generateAdapter = true)
data class FcmTokenResponse(
    val id: String?,
    val token: String?,
    val deviceName: String?,
    val osType: String?,
    val registeredAt: Instant?
)

/**
 * Enum cho các loại thông báo từ database
 */
enum class NotificationType {
    GENERAL_ANNOUNCEMENT,
    NEW_PROMOTION,
    ORDER_PLACED,
    ORDER_STATUS_UPDATE,
    REPAIR_REQUEST_RECEIVED,
    REPAIR_STATUS_UPDATE,
    REPAIR_TECHNICIAN_MESSAGE
}

@JsonClass(generateAdapter = true)
data class NotificationResponse(
    @field:Json(name = "id")
    val id: String?,
    
    @field:Json(name = "title")
    val title: String?,
    
    @field:Json(name = "body")
    val body: String?,
    
    @field:Json(name = "createdAt")
    val createdAt: String?,
    
    @field:Json(name = "read")
    val read: Boolean? = false,
    
    @field:Json(name = "resourceId")
    val resourceId: String? = null,
    
    @field:Json(name = "type")
    val type: String? = null
)

package com.example.datn_mobile.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ZaloPayCreateResult(
    @field:Json(name = "orderUrl")
    val orderUrl: String
)

@JsonClass(generateAdapter = true)
data class PaymentStatus(
    @field:Json(name = "orderId")
    val orderId: String,
    @field:Json(name = "status")
    val status: Int,
    @field:Json(name = "statusText")
    val statusText: String? = null
)



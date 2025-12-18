package com.example.datn_mobile.data.network.api

import com.example.datn_mobile.data.network.dto.ApiResponse
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApiService {

    /**
     * Tạo giao dịch ZaloPay cho đơn hàng
     * POST /bej3/payment/zalopay/create
     *
     * Body: { "orderId": "<orderId>" }
     * Response.result: { "orderUrl": "<url thanh toán>" }
     */
    @POST("/bej3/payment/zalopay/create")
    suspend fun createZaloPayPayment(
        @Body request: CreateZaloPayPaymentRequest
    ): Response<ApiResponse<CreateZaloPayPaymentResponse>>

    /**
     * Lấy trạng thái thanh toán của 1 đơn hàng
     * GET /bej3/payment/status/{orderId}
     *
     * Response.result: { "orderId": "...", "status": 0|1|2, "statusText": "..." }
     */
    @GET("/bej3/payment/status/{orderId}")
    suspend fun getPaymentStatus(
        @Path("orderId") orderId: String
    ): Response<ApiResponse<PaymentStatusResponse>>
}

@JsonClass(generateAdapter = true)
data class CreateZaloPayPaymentRequest(
    val orderId: String
)

@JsonClass(generateAdapter = true)
data class CreateZaloPayPaymentResponse(
    val orderUrl: String
)

@JsonClass(generateAdapter = true)
data class PaymentStatusResponse(
    val orderId: String,
    val status: Int,
    val statusText: String? = null
)



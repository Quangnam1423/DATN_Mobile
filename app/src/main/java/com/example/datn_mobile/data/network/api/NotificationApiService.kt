package com.example.datn_mobile.data.network.api

import com.example.datn_mobile.data.network.dto.ApiResponse
import com.example.datn_mobile.data.network.dto.FcmTokenResponse
import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.data.network.dto.RegisterFcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationApiService {

    @POST("/bej3/api/device-token/register")
    suspend fun registerDeviceToken(@Body request: RegisterFcmTokenRequest): Response<ApiResponse<FcmTokenResponse>>

    @DELETE("/bej3/api/device-token/all")
    suspend fun deleteAllTokens(): Response<ApiResponse<Unit>>

    @GET("/bej3/api/notifications/my-notifications")
    suspend fun getMyNotifications(): Response<ApiResponse<List<NotificationResponse>>>

    @PUT("/bej3/api/notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String
    ): Response<ApiResponse<Unit>>
}

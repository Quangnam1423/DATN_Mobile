package com.example.datn_mobile.data.repository

import android.os.Build
import android.util.Log
import com.example.datn_mobile.data.network.api.NotificationApiService
import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.data.network.dto.RegisterFcmTokenRequest
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService
) : NotificationRepository {

    override suspend fun registerDeviceToken(fcmToken: String): Resource<Unit> {
        return try {
            Log.d("NotificationRepository", "Registering device token: $fcmToken")
            val request = RegisterFcmTokenRequest(
                token = fcmToken,
                deviceName = Build.MODEL,
                osType = "Android"
            )
            Log.d("NotificationRepository", "Request: $request")
            val response = notificationApiService.registerDeviceToken(request)
            Log.d("NotificationRepository", "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")
            if (response.isSuccessful) {
                Log.d("NotificationRepository", "Device token registered successfully")
                Resource.Success(Unit)
            } else {
                Log.e("NotificationRepository", "Failed to register token: ${response.message()}")
                Resource.Error("Failed to register token: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error registering device token", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun deleteAllTokens(): Resource<Unit> {
        return try {
            Log.d("NotificationRepository", "Deleting all tokens...")
            val response = notificationApiService.deleteAllTokens()
            if (response.isSuccessful) {
                Log.d("NotificationRepository", "All tokens deleted successfully")
                Resource.Success(Unit)
            } else {
                Log.e("NotificationRepository", "Failed to delete tokens: ${response.message()}")
                Resource.Error("Failed to delete tokens: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error deleting tokens", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getMyNotifications(): Resource<List<NotificationResponse>> {
        return try {
            Log.d("NotificationRepository", "Fetching my notifications...")
            val response = notificationApiService.getMyNotifications()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.result != null) {
                    val notifications: List<NotificationResponse> = apiResponse.result
                    Log.d("NotificationRepository", "Fetched ${notifications.size} notifications")
                    Resource.Success(notifications)
                } else {
                    Log.d("NotificationRepository", "No notifications found")
                    Resource.Success(emptyList())
                }
            } else {
                Log.e("NotificationRepository", "Failed to fetch notifications: ${response.message()}")
                Resource.Error("Failed to fetch notifications: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error fetching notifications", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun markAsRead(notificationId: String): Resource<Unit> {
        return try {
            Log.d("NotificationRepository", "Marking notification $notificationId as read...")
            val response = notificationApiService.markNotificationAsRead(notificationId)
            if (response.isSuccessful) {
                Log.d("NotificationRepository", "Notification marked as read")
                Resource.Success(Unit)
            } else {
                Log.e("NotificationRepository", "Failed to mark notification as read: ${response.message()}")
                Resource.Error("Failed to mark notification as read: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error marking notification as read", e)
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}

package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.repository.NotificationRepository
import javax.inject.Inject

class GetMyNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Resource<List<NotificationResponse>> {
        return notificationRepository.getMyNotifications()
    }
}


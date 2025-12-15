package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Resource<Unit> {
        if (notificationId.isBlank()) {
            return Resource.Error("ID thông báo không hợp lệ")
        }
        return notificationRepository.markAsRead(notificationId)
    }
}



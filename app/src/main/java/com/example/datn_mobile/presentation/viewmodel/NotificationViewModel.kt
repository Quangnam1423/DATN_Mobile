package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.usecase.GetMyNotificationsUseCase
import com.example.datn_mobile.domain.usecase.MarkNotificationAsReadUseCase
import com.example.datn_mobile.domain.usecase.ConfirmRepairOrderUseCase
import com.example.datn_mobile.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationState(
    val notifications: List<NotificationResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isConfirming: Boolean = false,
    val confirmedOrderId: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val confirmRepairOrderUseCase: ConfirmRepairOrderUseCase
) : ViewModel() {

    private val _notificationState = MutableStateFlow(NotificationState())
    val notificationState = _notificationState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _notificationState.value = _notificationState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getMyNotificationsUseCase()) {
                is Resource.Success -> {
                    _notificationState.value = _notificationState.value.copy(
                        notifications = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _notificationState.value = _notificationState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                    MessageManager.showError(result.message ?: "Không thể tải thông báo")
                }
                is Resource.Loading -> {
                    _notificationState.value = _notificationState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun getUnreadCount(): Int {
        return _notificationState.value.notifications.count { it.read == false }
    }

    fun markAsRead(notification: NotificationResponse) {
        val id = notification.id ?: return

        viewModelScope.launch {
            val current = _notificationState.value.notifications
            val updated = current.map {
                if (it.id == id) it.copy(read = true) else it
            }
            _notificationState.value = _notificationState.value.copy(notifications = updated)

            // Gọi API backend
            when (val result = markNotificationAsReadUseCase(id)) {
                is Resource.Error -> {
                    MessageManager.showError(result.message ?: "Không thể đánh dấu thông báo đã đọc")
                }
                else -> Unit
            }
        }
    }

    fun confirmRepairOrder(orderId: String, notificationId: String? = null) {
        if (orderId.isBlank()) {
            MessageManager.showError("ID đơn hàng không hợp lệ")
            return
        }

        viewModelScope.launch {
            _notificationState.value = _notificationState.value.copy(
                isConfirming = true,
                error = null
            )

            when (val result = confirmRepairOrderUseCase(orderId)) {
                is Resource.Success -> {
                    // Mark notification as read if notificationId is provided
                    notificationId?.let { id ->
                        val current = _notificationState.value.notifications
                        val updated = current.map {
                            if (it.id == id) it.copy(read = true) else it
                        }
                        _notificationState.value = _notificationState.value.copy(notifications = updated)
                        markNotificationAsReadUseCase(id)
                    }
                    
                    _notificationState.value = _notificationState.value.copy(
                        isConfirming = false,
                        confirmedOrderId = orderId
                    )
                    MessageManager.showSuccess("Đã xác nhận thành công! Chúng tôi sẽ tiến hành sửa chữa ngay.")
                    // Reload notifications to update status
                    loadNotifications()
                }
                is Resource.Error -> {
                    _notificationState.value = _notificationState.value.copy(
                        isConfirming = false,
                        error = result.message
                    )
                    MessageManager.showError(result.message ?: "Không thể xác nhận đơn hàng")
                }
                is Resource.Loading -> {
                    _notificationState.value = _notificationState.value.copy(
                        isConfirming = true
                    )
                }
            }
        }
    }
}


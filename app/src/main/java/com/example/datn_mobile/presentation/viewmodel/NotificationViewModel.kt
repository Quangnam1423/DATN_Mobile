package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.usecase.GetMyNotificationsUseCase
import com.example.datn_mobile.domain.usecase.MarkNotificationAsReadUseCase
import com.example.datn_mobile.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationState(
    val notifications: List<NotificationResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase
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
}


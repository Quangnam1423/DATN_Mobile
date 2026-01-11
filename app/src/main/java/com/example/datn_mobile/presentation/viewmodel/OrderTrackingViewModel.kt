package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.domain.model.PaymentStatus
import com.example.datn_mobile.domain.usecase.GetMyOrdersUseCase
import com.example.datn_mobile.domain.usecase.GetPaymentStatusUseCase
import com.example.datn_mobile.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderTrackingState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedOrder: Order? = null,
    val paymentStatuses: Map<String, PaymentStatus> = emptyMap() // orderId -> payment status
)

@HiltViewModel
class OrderTrackingViewModel @Inject constructor(
    private val getMyOrdersUseCase: GetMyOrdersUseCase,
    private val getPaymentStatusUseCase: GetPaymentStatusUseCase
) : ViewModel() {

    private val _orderTrackingState = MutableStateFlow(OrderTrackingState())
    val orderTrackingState = _orderTrackingState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _orderTrackingState.value = OrderTrackingState(isLoading = true)

            when (val result = getMyOrdersUseCase()) {
                is Resource.Success -> {
                    val orders = result.data ?: emptyList()
                    _orderTrackingState.value = OrderTrackingState(orders = orders)

                    // Sau khi có danh sách đơn, load trạng thái thanh toán cho từng order ở background
                    loadPaymentStatusesForOrders(orders)
                }

                is Resource.Error -> {
                    val errorMessage = result.message ?: "Lỗi không xác định khi tải đơn hàng"
                    _orderTrackingState.value = OrderTrackingState(error = errorMessage)
                    MessageManager.showError(errorMessage)
                }

                is Resource.Loading -> {
                    // Đang tải, không làm gì
                }
            }
        }
    }

    fun refreshOrders() {
        loadOrders()
    }

    fun selectOrder(order: Order) {
        _orderTrackingState.value = _orderTrackingState.value.copy(selectedOrder = order)
    }

    fun getOrderById(orderId: String): Order? {
        return _orderTrackingState.value.orders.find { it.id == orderId }
    }

    /**
     * Lấy PaymentStatus theo orderId từ state
     */
    fun getPaymentStatus(orderId: String): PaymentStatus? {
        return _orderTrackingState.value.paymentStatuses[orderId]
    }

    /**
     * Gọi API /payment/status/{orderId} cho tất cả orders và lưu map vào state.
     * Chạy song song, không block UI chính.
     */
    private fun loadPaymentStatusesForOrders(orders: List<Order>) {
        if (orders.isEmpty()) return

        viewModelScope.launch {
            val currentMap = _orderTrackingState.value.paymentStatuses.toMutableMap()

            for (order in orders) {
                try {
                    when (val result = getPaymentStatusUseCase(order.id)) {
                        is Resource.Success -> {
                            val status = result.data ?: continue
                            currentMap[order.id] = status
                        }
                        is Resource.Error -> {
                            // Bỏ qua lỗi, không chặn việc hiển thị danh sách đơn
                        }
                        is Resource.Loading -> Unit
                    }
                } catch (_: Exception) {
                    // Bỏ qua lỗi đơn lẻ
                }
            }

            _orderTrackingState.value =
                _orderTrackingState.value.copy(paymentStatuses = currentMap)
        }
    }
}


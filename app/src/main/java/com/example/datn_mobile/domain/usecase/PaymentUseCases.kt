package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.PaymentStatus
import com.example.datn_mobile.domain.model.ZaloPayCreateResult
import com.example.datn_mobile.domain.repository.PaymentRepository
import javax.inject.Inject

/**
 * Tạo giao dịch ZaloPay cho một đơn hàng
 * POST /bej3/payment/zalopay/create
 */
class CreateZaloPayPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(orderId: String): Resource<ZaloPayCreateResult> {
        if (orderId.isBlank()) {
            return Resource.Error("OrderId không hợp lệ")
        }
        return paymentRepository.createZaloPayPayment(orderId)
    }
}

/**
 * Lấy trạng thái thanh toán
 * GET /bej3/payment/status/{orderId}
 */
class GetPaymentStatusUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(orderId: String): Resource<PaymentStatus> {
        if (orderId.isBlank()) {
            return Resource.Error("OrderId không hợp lệ")
        }
        return paymentRepository.getPaymentStatus(orderId)
    }
}



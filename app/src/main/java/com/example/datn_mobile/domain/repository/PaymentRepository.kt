package com.example.datn_mobile.domain.repository

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.PaymentStatus
import com.example.datn_mobile.domain.model.ZaloPayCreateResult

interface PaymentRepository {

    /**
     * Tạo giao dịch ZaloPay cho 1 đơn hàng
     */
    suspend fun createZaloPayPayment(orderId: String): Resource<ZaloPayCreateResult>

    /**
     * Lấy trạng thái thanh toán của 1 đơn hàng
     */
    suspend fun getPaymentStatus(orderId: String): Resource<PaymentStatus>
}



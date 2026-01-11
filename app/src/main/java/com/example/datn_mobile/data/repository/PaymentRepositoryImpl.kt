package com.example.datn_mobile.data.repository

import com.example.datn_mobile.data.network.api.CreateZaloPayPaymentRequest
import com.example.datn_mobile.data.network.api.PaymentApiService
import com.example.datn_mobile.data.network.api.PaymentStatusResponse
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.PaymentStatus
import com.example.datn_mobile.domain.model.ZaloPayCreateResult
import com.example.datn_mobile.domain.repository.PaymentRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val paymentApiService: PaymentApiService
) : PaymentRepository {

    override suspend fun createZaloPayPayment(orderId: String): Resource<ZaloPayCreateResult> {
        return try {
            val response = paymentApiService.createZaloPayPayment(
                CreateZaloPayPaymentRequest(orderId = orderId)
            )
            if (response.isSuccessful) {
                val apiResponse = response.body()
                val result = apiResponse?.result
                if (result != null && result.orderUrl.isNotBlank()) {
                    Resource.Success(ZaloPayCreateResult(orderUrl = result.orderUrl))
                } else {
                    Resource.Error(apiResponse?.message ?: "Không nhận được URL thanh toán ZaloPay")
                }
            } else {
                Resource.Error("Lỗi tạo giao dịch ZaloPay: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Lỗi mạng: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Lỗi kết nối: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Lỗi không xác định: ${e.message}")
        }
    }

    override suspend fun getPaymentStatus(orderId: String): Resource<PaymentStatus> {
        return try {
            val response = paymentApiService.getPaymentStatus(orderId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                val result = apiResponse?.result
                if (result != null) {
                    Resource.Success(result.toPaymentStatusDomain())
                } else {
                    Resource.Error(apiResponse?.message ?: "Không nhận được trạng thái thanh toán")
                }
            } else {
                Resource.Error("Lỗi lấy trạng thái thanh toán: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Lỗi mạng: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Lỗi kết nối: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Lỗi không xác định: ${e.message}")
        }
    }
}

private fun PaymentStatusResponse.toPaymentStatusDomain(): PaymentStatus {
    return PaymentStatus(
        orderId = orderId,
        status = status,
        statusText = statusText
    )
}



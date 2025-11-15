package com.example.datn_mobile.data.repository

import com.example.datn_mobile.data.network.api.ProductApiService
import com.example.datn_mobile.domain.model.HomeResponse
import com.example.datn_mobile.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {
    override suspend fun getHomeProducts(): HomeResponse {
        val response = apiService.getHomeProducts()
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception(response.message())
        }
    }

    override suspend fun getProductDetail(productId: String): HomeResponse {
        val response = apiService.getProductDetail(productId)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception(response.message())
        }
    }
}


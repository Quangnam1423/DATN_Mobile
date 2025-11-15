package com.example.datn_mobile.domain.repository

import com.example.datn_mobile.domain.model.HomeResponse

interface ProductRepository {
    suspend fun getHomeProducts(): HomeResponse
    suspend fun getProductDetail(productId: String): HomeResponse
}


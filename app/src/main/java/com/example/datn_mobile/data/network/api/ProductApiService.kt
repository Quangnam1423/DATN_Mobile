package com.example.datn_mobile.data.network.api

import com.example.datn_mobile.domain.model.HomeResponse
import com.example.datn_mobile.data.network.interceptor.NoAuth
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface ProductApiService {
    @NoAuth
    @GET("/bej3/home")
    suspend fun getHomeProducts(): Response<HomeResponse>

    @NoAuth
    @GET("/bej3/home/product/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: String): Response<HomeResponse>
}


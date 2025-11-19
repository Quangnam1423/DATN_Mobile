package com.example.datn_mobile.data.repository

import com.example.datn_mobile.data.network.api.ProductApiService
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Product
import com.example.datn_mobile.domain.repository.ProductRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : ProductRepository {
    override suspend fun getHomeProducts(): Resource<List<Product>> {
        return try {
            val response = apiService.getHomeProducts()
            if (response.isSuccessful) {
                val homeResponse = response.body()
                if (homeResponse?.result != null) {
                    Resource.Success(homeResponse.result)
                } else {
                    Resource.Error("Response body is empty or null")
                }
            } else {
                Resource.Error("Failed to fetch home products: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Connection error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unknown error: ${e.message}")
        }
    }

    override suspend fun getProductDetail(productId: String): Resource<Product> {
        return try {
            val response = apiService.getProductDetail(productId)
            if (response.isSuccessful) {
                val homeResponse = response.body()
                if (homeResponse?.result?.isNotEmpty() == true) {
                    Resource.Success(homeResponse.result[0])
                } else {
                    Resource.Error("Product not found")
                }
            } else {
                Resource.Error("Failed to fetch product detail: ${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Connection error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unknown error: ${e.message}")
        }
    }
}


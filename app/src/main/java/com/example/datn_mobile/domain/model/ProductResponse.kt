package com.example.datn_mobile.domain.model

data class ProductVariant(
    val originalPrice: Long? = null,
    val finalPrice: Long? = null,
    val thumbnail: String? = null,
    val color: String? = null,
    val price: Long? = null,
    val quantity: Int? = null
)

data class Product(
    val id: String,
    val name: String,
    val image: String,
    val status: Int,
    val createDate: String,
    val variant: ProductVariant
)

data class HomeResponse(
    val result: List<Product>,
    val code: Int,
    val message: String? = null
)
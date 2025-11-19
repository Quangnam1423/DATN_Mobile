package com.example.datn_mobile.domain.model

/**
 * Đại diện 1 sản phẩm trong giỏ hàng
 * Tương ứng với CartItemResponse từ API
 */
data class CartItem(
    val id: String,                  // ID CartItem
    val attId: String,               // ID ProductAttribute
    val productAttName: String,      // Tên biến thể (VD: "12 / 256 GB")
    val quantity: Int,               // Số lượng
    val price: Long,                 // Giá sản phẩm
    val color: String,               // Màu sắc
    val productName: String,         // Tên sản phẩm
    val img: String                  // URL ảnh
)

/**
 * Giỏ hàng của user
 * Chứa danh sách CartItem
 */
data class Cart(
    val id: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Long = 0,
    val totalQuantity: Int = 0
)

/**
 * Đơn hàng của user
 * Tương ứng với OrderDetailsResponse từ API
 */
data class Order(
    val id: String,                  // ID đơn hàng
    val userName: String,            // Tên người dùng
    val phoneNumber: String,         // SĐT giao hàng
    val email: String,               // Email giao hàng
    val address: String,             // Địa chỉ giao hàng
    val description: String? = null, // Ghi chú
    val totalPrice: Long,            // Tổng tiền
    val orderAt: String,             // Ngày đặt hàng (YYYY-MM-DD)
    val updatedAt: String? = null,   // Ngày cập nhật
    val orderItems: List<OrderItem> = emptyList()  // Danh sách sản phẩm
)

/**
 * Chi tiết sản phẩm trong đơn hàng
 * Tương ứng với OrderItemResponse từ API
 */
data class OrderItem(
    val productAttName: String,      // Tên biến thể
    val quantity: Int,               // Số lượng
    val price: Long,                 // Giá
    val color: String,               // Màu sắc
    val productName: String,         // Tên sản phẩm
    val img: String                  // URL ảnh
)


package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Cart
import com.example.datn_mobile.domain.model.CartItem
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.domain.repository.CartRepository
import javax.inject.Inject

/**
 * 1️⃣ Thêm sản phẩm vào giỏ hàng
 * POST /bej3/cart/add/{attId}
 */
class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(attId: String): Resource<CartItem> {
        if (attId.isBlank()) {
            return Resource.Error("Vui lòng chọn sản phẩm hợp lệ")
        }
        return cartRepository.addToCart(attId)
    }
}

/**
 * 2️⃣ Xem danh sách giỏ hàng
 * GET /bej3/cart/view
 */
class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Resource<Cart> {
        return cartRepository.getCart()
    }
}

/**
 * 3️⃣ Đặt hàng (Place Order)
 * POST /bej3/cart/place-order
 */
class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(
        type: Int = 0, // 0 = mua, 1 = sửa
        phoneNumber: String,
        email: String,
        address: String,
        description: String?,
        totalPrice: Long,
        items: List<Pair<String, String>>  // Pair<cartItemId, productAttId>
    ): Resource<Order> {
        if (type !in 0..1) {
            return Resource.Error("Loại đơn hàng không hợp lệ")
        }
        // Validate input
        if (phoneNumber.isBlank()) {
            return Resource.Error("Vui lòng nhập số điện thoại")
        }

        if (email.isBlank()) {
            return Resource.Error("Vui lòng nhập email")
        }

        // type 0 = mua: yêu cầu địa chỉ, totalPrice > 0, items không rỗng
        if (type == 0) {
            if (address.isBlank()) {
                return Resource.Error("Vui lòng nhập địa chỉ giao hàng")
            }
            if (totalPrice <= 0) {
                return Resource.Error("Tổng tiền phải lớn hơn 0")
            }
            if (items.isEmpty()) {
                return Resource.Error("Giỏ hàng trống")
            }
        }
        // type 1 = sửa: cho phép bỏ qua address/totalPrice/items

        return cartRepository.placeOrder(
            type,
            phoneNumber,
            email,
            address,
            description,
            totalPrice,
            items
        )
    }
}

/**
 * 4️⃣ Xem lịch sử đơn hàng
 * GET /bej3/cart/my-order
 */
class GetMyOrdersUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Resource<List<Order>> {
        return cartRepository.getMyOrders()
    }
}

/**
 * 6️⃣ Cập nhật số lượng 1 sản phẩm trong giỏ hàng
 * PUT /bej3/cart/update/{cartItemId}?quantity={quantity}
 */
class UpdateCartItemQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String, quantity: Int): Resource<CartItem> {
        if (cartItemId.isBlank()) {
            return Resource.Error("ID sản phẩm trong giỏ không hợp lệ")
        }
        if (quantity !in 1..10) {
            return Resource.Error("Số lượng phải nằm trong khoảng 1 đến 10")
        }
        return cartRepository.updateCartItemQuantity(cartItemId, quantity)
    }
}



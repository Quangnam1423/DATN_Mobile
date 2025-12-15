package com.example.datn_mobile.domain.usecase

import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.repository.CartRepository
import javax.inject.Inject

/**
 * 5️⃣ Xóa 1 sản phẩm khỏi giỏ hàng
 * DELETE /bej3/cart/remove/{cartItemId}
 */
class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartItemId: String): Resource<Unit> {
        if (cartItemId.isBlank()) {
            return Resource.Error("ID sản phẩm trong giỏ không hợp lệ")
        }
        return cartRepository.removeFromCart(cartItemId)
    }
}



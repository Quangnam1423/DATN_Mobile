package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Cart
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.domain.usecase.AddToCartUseCase
import com.example.datn_mobile.domain.usecase.GetCartUseCase
import com.example.datn_mobile.domain.usecase.GetMyOrdersUseCase
import com.example.datn_mobile.domain.usecase.PlaceOrderUseCase
import com.example.datn_mobile.utils.MessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false
)

data class OrderState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val getMyOrdersUseCase: GetMyOrdersUseCase
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    init {
        loadCart()
    }

    /**
     * 1️⃣ Tải giỏ hàng từ backend
     * GET /bej3/cart/view
     */
    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState(isLoading = true)

            when (val result = getCartUseCase()) {
                is Resource.Success -> {
                    _cartState.value = CartState(cart = result.data)
                }
                is Resource.Error -> {
                    _cartState.value = CartState(error = result.message)
                    MessageManager.showError(result.message ?: "Lỗi tải giỏ hàng")
                }
                else -> {}
            }
        }
    }

    /**
     * 2️⃣ Thêm sản phẩm vào giỏ hàng
     * POST /bej3/cart/add/{attId}
     */
    fun addToCart(attId: String) {
        viewModelScope.launch {
            _cartState.value = _cartState.value.copy(isUpdating = true)

            when (val result = addToCartUseCase(attId)) {
                is Resource.Success -> {
                    MessageManager.showSuccess("✅ Thêm vào giỏ hàng thành công")
                    // Reload cart after adding
                    loadCart()
                }
                is Resource.Error -> {
                    _cartState.value = _cartState.value.copy(
                        error = result.message,
                        isUpdating = false
                    )
                    MessageManager.showError(result.message ?: "Lỗi thêm vào giỏ")
                }
                else -> {}
            }
        }
    }

    /**
     * 3️⃣ Đặt hàng
     * POST /bej3/cart/place-order
     */
    fun placeOrder(
        phoneNumber: String,
        email: String,
        address: String,
        description: String?,
        totalPrice: Long,
        items: List<Pair<String, String>>  // Pair<cartItemId, productAttId>
    ) {
        viewModelScope.launch {
            _cartState.value = _cartState.value.copy(isUpdating = true)

            when (val result = placeOrderUseCase(phoneNumber, email, address, description, totalPrice, items)) {
                is Resource.Success -> {
                    MessageManager.showSuccess("✅ Đặt hàng thành công")
                    // Clear cart after successful order
                    _cartState.value = CartState()
                    // Reload orders
                    loadMyOrders()
                }
                is Resource.Error -> {
                    _cartState.value = _cartState.value.copy(
                        error = result.message,
                        isUpdating = false
                    )
                    MessageManager.showError(result.message ?: "Lỗi đặt hàng")
                }
                else -> {}
            }
        }
    }

    /**
     * 4️⃣ Xem lịch sử đơn hàng
     * GET /bej3/cart/my-order
     */
    fun loadMyOrders() {
        viewModelScope.launch {
            _orderState.value = OrderState(isLoading = true)

            when (val result = getMyOrdersUseCase()) {
                is Resource.Success -> {
                    _orderState.value = OrderState(orders = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _orderState.value = OrderState(error = result.message)
                    MessageManager.showError(result.message ?: "Lỗi tải đơn hàng")
                }
                else -> {}
            }
        }
    }
}




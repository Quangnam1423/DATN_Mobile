package com.example.datn_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datn_mobile.data.util.Resource
import com.example.datn_mobile.domain.model.Cart
import com.example.datn_mobile.domain.model.CartItem
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.domain.usecase.AddToCartUseCase
import com.example.datn_mobile.domain.usecase.GetCartUseCase
import com.example.datn_mobile.domain.usecase.GetMyOrdersUseCase
import com.example.datn_mobile.domain.usecase.PlaceOrderUseCase
import com.example.datn_mobile.domain.usecase.RemoveFromCartUseCase
import com.example.datn_mobile.domain.usecase.UpdateCartItemQuantityUseCase
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
    val isUpdating: Boolean = false,
    val totalPrice: Long = 0,          // Tổng giá tiền giỏ hàng
    val totalQuantity: Int = 0         // Tổng số lượng sản phẩm
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
    private val getMyOrdersUseCase: GetMyOrdersUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    init {
        loadCart()
    }

    /**
     * Tính tổng giá tiền và số lượng từ danh sách items
     * @param items Danh sách sản phẩm trong giỏ hàng
     * @return Pair<totalPrice, totalQuantity>
     */
    private fun calculateCartTotals(items: List<CartItem>?): Pair<Long, Int> {
        if (items.isNullOrEmpty()) {
            return Pair(0L, 0)
        }

        var totalPrice = 0L
        var totalQuantity = 0

        try {
            for (item in items) {
                // Kiểm tra giá và số lượng hợp lệ
                if (item.price >= 0 && item.quantity > 0) {
                    totalPrice += item.price * item.quantity
                    totalQuantity += item.quantity
                }
            }
        } catch (e: Exception) {
            MessageManager.showError("Lỗi tính toán tổng giỏ hàng: ${e.message}")
        }

        return Pair(totalPrice, totalQuantity)
    }

    /**
     * 1️⃣ Tải giỏ hàng từ backend
     * GET /bej3/cart/view
     * Response: {
     *   "result": [CartItem],
     *   "code": 1000,
     *   "message": "Success"
     * }
     */
    fun loadCart() {
        viewModelScope.launch {
            try {
                _cartState.value = CartState(isLoading = true)

                when (val result = getCartUseCase()) {
                    is Resource.Success -> {
                        val cartData = result.data

                        // Validate dữ liệu từ API
                        if (cartData == null || cartData.items.isEmpty()) {
                            _cartState.value = CartState(
                                cart = cartData ?: Cart(items = emptyList()),
                                totalPrice = 0L,
                                totalQuantity = 0
                            )
                        } else {
                            // Tính tổng tiền và số lượng
                            val (totalPrice, totalQuantity) = calculateCartTotals(cartData.items)
                            _cartState.value = CartState(
                                cart = cartData,
                                totalPrice = totalPrice,
                                totalQuantity = totalQuantity
                            )
                        }
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi tải giỏ hàng"
                        _cartState.value = CartState(error = errorMessage)
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> {
                        // Trạng thái đang tải, không làm gì
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _cartState.value = CartState(error = errorMsg)
                MessageManager.showError(errorMsg)
            }
        }
    }

    /**
     * 2️⃣ Thêm sản phẩm vào giỏ hàng
     * POST /bej3/cart/add/{attId}
     * Validation: attId không được rỗng
     */
    fun addToCart(attId: String) {
        // Validate input
        if (attId.isBlank()) {
            MessageManager.showError("❌ Lỗi: ID thuộc tính sản phẩm không được rỗng")
            return
        }

        viewModelScope.launch {
            try {
                _cartState.value = _cartState.value.copy(isUpdating = true)

                when (val result = addToCartUseCase(attId.trim())) {
                    is Resource.Success -> {
                        MessageManager.showSuccess("✅ Thêm vào giỏ hàng thành công")
                        // Reload cart after adding to update totals
                        loadCart()
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi thêm vào giỏ"
                        _cartState.value = _cartState.value.copy(
                            error = errorMessage,
                            isUpdating = false
                        )
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> {
                        // Đang xử lý, không làm gì
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _cartState.value = _cartState.value.copy(
                    error = errorMsg,
                    isUpdating = false
                )
                MessageManager.showError(errorMsg)
            }
        }
    }

    /**
     * 3️⃣ Đặt hàng
     * POST /bej3/cart/place-order
     * Validation: Kiểm tra tất cả các tham số bắt buộc
     */
    fun placeOrder(
        type: Int = 0, // 0 = mua, 1 = sửa
        phoneNumber: String,
        email: String,
        address: String,
        description: String?,
        totalPrice: Long,
        items: List<Pair<String, String>>  // Pair<cartItemId, productAttId>
    ) {
        // Validate input (phân nhánh theo type)
        val validationError = validateOrderInput(type, phoneNumber, email, address, totalPrice, items)
        if (validationError != null) {
            MessageManager.showError(validationError)
            return
        }

        viewModelScope.launch {
            try {
                _cartState.value = _cartState.value.copy(isUpdating = true)

                when (val result = placeOrderUseCase(
                    type,
                    phoneNumber.trim(),
                    email.trim(),
                    address.trim(),
                    description?.trim(),
                    totalPrice,
                    items
                )) {
                    is Resource.Success -> {
                        MessageManager.showSuccess("✅ Đặt hàng thành công")
                        // Clear cart after successful order
                        _cartState.value = CartState()
                        // Reload orders
                        loadMyOrders()
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi đặt hàng"
                        _cartState.value = _cartState.value.copy(
                            error = errorMessage,
                            isUpdating = false
                        )
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> {
                        // Đang xử lý, không làm gì
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _cartState.value = _cartState.value.copy(
                    error = errorMsg,
                    isUpdating = false
                )
                MessageManager.showError(errorMsg)
            }
        }
    }

    /**
     * 5️⃣ Xóa 1 sản phẩm khỏi giỏ hàng
     * DELETE /bej3/cart/remove/{cartItemId}
     */
    fun removeFromCart(cartItemId: String) {
        if (cartItemId.isBlank()) {
            MessageManager.showError("❌ ID sản phẩm trong giỏ không hợp lệ")
            return
        }

        viewModelScope.launch {
            try {
                _cartState.value = _cartState.value.copy(isUpdating = true)

                when (val result = removeFromCartUseCase(cartItemId.trim())) {
                    is Resource.Success -> {
                        MessageManager.showSuccess("🗑️ Đã xóa sản phẩm khỏi giỏ")
                        // Backend không trả về tổng mới → gọi lại GET /bej3/cart/view
                        loadCart()
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi xóa khỏi giỏ"
                        _cartState.value = _cartState.value.copy(
                            error = errorMessage,
                            isUpdating = false
                        )
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> {
                        // Đang xử lý, không làm gì
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _cartState.value = _cartState.value.copy(
                    error = errorMsg,
                    isUpdating = false
                )
                MessageManager.showError(errorMsg)
            }
        }
    }

    /**
     * 6️⃣ Tăng số lượng sản phẩm trong giỏ hàng (tối đa 10)
     */
    fun increaseQuantity(item: CartItem) {
        val currentQty = item.quantity
        val newQty = (currentQty + 1).coerceAtMost(10)
        if (newQty == currentQty) {
            MessageManager.showError("Số lượng tối đa cho mỗi sản phẩm là 10")
            return
        }
        updateItemQuantity(item, newQty)
    }

    /**
     * 7️⃣ Giảm số lượng sản phẩm trong giỏ hàng (tối thiểu 1)
     */
    fun decreaseQuantity(item: CartItem) {
        val currentQty = item.quantity
        val newQty = (currentQty - 1).coerceAtLeast(1)
        if (newQty == currentQty) {
            return
        }
        updateItemQuantity(item, newQty)
    }

    /**
     * Gọi API cập nhật số lượng và cập nhật lại cart local + tổng tiền/số lượng
     */
    private fun updateItemQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            try {
                _cartState.value = _cartState.value.copy(isUpdating = true)

                when (val result = updateCartItemQuantityUseCase(item.id, newQuantity)) {
                    is Resource.Success -> {
                        val updatedItem = result.data ?: return@launch
                        val currentCart = _cartState.value.cart

                        if (currentCart != null) {
                            val updatedItems = currentCart.items.map { cartItem ->
                                if (cartItem.id == item.id) updatedItem else cartItem
                            }

                            val (totalPrice, totalQuantity) = calculateCartTotals(updatedItems)

                            _cartState.value = _cartState.value.copy(
                                cart = currentCart.copy(
                                    items = updatedItems,
                                    totalPrice = totalPrice,
                                    totalQuantity = totalQuantity
                                ),
                                totalPrice = totalPrice,
                                totalQuantity = totalQuantity,
                                isUpdating = false
                            )
                        } else {
                            // Nếu vì lý do nào đó cart null, reload lại từ API
                            loadCart()
                        }
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi cập nhật số lượng"
                        _cartState.value = _cartState.value.copy(
                            error = errorMessage,
                            isUpdating = false
                        )
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> Unit
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _cartState.value = _cartState.value.copy(
                    error = errorMsg,
                    isUpdating = false
                )
                MessageManager.showError(errorMsg)
            }
        }
    }

    /**
     * Validate thông tin đặt hàng
     * @return Error message nếu validation thất bại, null nếu hợp lệ
     */
    private fun validateOrderInput(
        type: Int,
        phoneNumber: String,
        email: String,
        address: String,
        totalPrice: Long,
        items: List<Pair<String, String>>
    ): String? {
        // Kiểm tra số điện thoại
        if (phoneNumber.isBlank()) {
            return "❌ Số điện thoại không được rỗng"
        }
        if (!isValidPhoneNumber(phoneNumber.trim())) {
            return "❌ Số điện thoại không hợp lệ"
        }

        // Kiểm tra email
        if (email.isBlank()) {
            return "❌ Email không được rỗng"
        }
        if (!isValidEmail(email.trim())) {
            return "❌ Email không hợp lệ"
        }

        // type 0 = mua: yêu cầu địa chỉ, totalPrice > 0, items không rỗng
        if (type == 0) {
            if (address.isBlank()) {
                return "❌ Địa chỉ không được rỗng"
            }
            if (totalPrice <= 0) {
                return "❌ Tổng tiền phải lớn hơn 0"
            }
            if (items.isEmpty()) {
                return "❌ Giỏ hàng không có sản phẩm"
            }
        }
        // type 1 = sửa: cho phép bỏ qua address/totalPrice/items (ẩn trên UI)

        // Kiểm tra từng item
        if (items.isNotEmpty()) {
            for (item in items) {
                if (item.first.isBlank() || item.second.isBlank()) {
                    return "❌ ID sản phẩm hoặc ID thuộc tính không được rỗng"
                }
            }
        }

        return null
    }

    /**
     * Validate số điện thoại (format: 10 chữ số hoặc bắt đầu với +84)
     */
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^0\\d{9}$|^\\+84\\d{9}$"))
    }

    /**
     * Validate email (basic regex)
     */
    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"))
    }

    /**
     * 4️⃣ Xem lịch sử đơn hàng
     * GET /bej3/cart/my-order
     */
    fun loadMyOrders() {
        viewModelScope.launch {
            try {
                _orderState.value = OrderState(isLoading = true)

                when (val result = getMyOrdersUseCase()) {
                    is Resource.Success -> {
                        val orders = result.data ?: emptyList()
                        _orderState.value = OrderState(orders = orders)
                    }

                    is Resource.Error -> {
                        val errorMessage = result.message ?: "Lỗi không xác định khi tải đơn hàng"
                        _orderState.value = OrderState(error = errorMessage)
                        MessageManager.showError(errorMessage)
                    }

                    is Resource.Loading -> {
                        // Đang tải, không làm gì
                    }
                }
            } catch (e: Exception) {
                val errorMsg = "Exception: ${e.message ?: "Lỗi không xác định"}"
                _orderState.value = OrderState(error = errorMsg)
                MessageManager.showError(errorMsg)
            }
        }
    }
}




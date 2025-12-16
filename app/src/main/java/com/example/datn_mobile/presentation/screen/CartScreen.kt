package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import com.example.datn_mobile.domain.model.Cart
import com.example.datn_mobile.domain.model.CartItem
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.viewmodel.CartViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    onContinueShoppingClick: () -> Unit = onBackClick
) {
    val cartState by viewModel.cartState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Header
        CartHeader(onBackClick = onBackClick)

        // Content
        if (cartState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (cartState.cart == null || cartState.cart!!.items.isEmpty()) {
            EmptyCartScreen(onContinueShopping = onContinueShoppingClick)
        } else {
            CartContent(
                cart = cartState.cart!!,
                isUpdating = cartState.isUpdating,
                totalPrice = cartState.totalPrice,
                totalQuantity = cartState.totalQuantity,
                onCheckout = onCheckoutClick,
                onRemoveItem = { item ->
                    viewModel.removeFromCart(item.id)
                },
                onIncreaseQuantity = { item ->
                    viewModel.increaseQuantity(item)
                },
                onDecreaseQuantity = { item ->
                    viewModel.decreaseQuantity(item)
                },
                selectedItemIds = cartState.selectedItemIds,
                onToggleSelect = { item ->
                    viewModel.toggleItemSelection(item)
                }
            )
        }
    }
}

@Composable
private fun CartHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PeachPinkAccent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Quay lại",
                tint = Color.White
            )
        }

        Text(
            text = "Giỏ Hàng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun EmptyCartScreen(onContinueShopping: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🛒",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Giỏ hàng của bạn trống",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Thêm sản phẩm để bắt đầu mua sắm",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = onContinueShopping,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
            ) {
                Text("Tiếp tục mua sắm")
            }
        }
    }
}

@Composable
private fun CartContent(
    cart: Cart,
    isUpdating: Boolean,
    totalPrice: Long,
    totalQuantity: Int,
    onCheckout: () -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onIncreaseQuantity: (CartItem) -> Unit,
    onDecreaseQuantity: (CartItem) -> Unit,
    selectedItemIds: Set<String>,
    onToggleSelect: (CartItem) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Cart items list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cart.items) { item ->
                CartItemCard(
                    item = item,
                    isSelected = selectedItemIds.contains(item.id),
                    onToggleSelect = { onToggleSelect(item) },
                    onRemoveClick = { onRemoveItem(item) },
                    onIncreaseClick = { onIncreaseQuantity(item) },
                    onDecreaseClick = { onDecreaseQuantity(item) }
                )
            }
        }

        // Summary & Checkout
        CartSummary(
            totalPrice = totalPrice,
            totalQuantity = totalQuantity,
            isUpdating = isUpdating,
            onCheckout = onCheckout
        )
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    isSelected: Boolean,
    onToggleSelect: () -> Unit,
    onRemoveClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checkbox chọn sản phẩm
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggleSelect() }
            )

            // Delete icon on the left
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Xóa khỏi giỏ hàng",
                    tint = Color.Red
                )
            }

            // Product info + quantity controls
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Tên sản phẩm
                Text(
                    text = item.productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                // Phân loại (dung lượng, màu, size...)
                if (item.productAttName.isNotBlank() || item.color.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (item.productAttName.isNotBlank()) {
                            Text(
                                text = item.productAttName,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        if (item.color.isNotBlank()) {
                            Text(
                                text = "• Màu: ${item.color}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Giá đơn vị
                Text(
                    text = "${item.price.toFormattedPrice()} đ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onDecreaseClick,
                        enabled = item.quantity > 1
                    ) {
                        Text(
                            text = "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (item.quantity > 1) PeachPinkAccent else Color.LightGray
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onIncreaseClick,
                        enabled = item.quantity < 10
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tăng số lượng",
                            tint = if (item.quantity < 10) PeachPinkAccent else Color.LightGray
                        )
                    }
                }
            }

            // Total price
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(
                    text = "${(item.price * item.quantity).toFormattedPrice()} đ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PeachPinkAccent
                )
            }
        }
    }
}

@Composable
private fun CartSummary(
    totalPrice: Long,
    totalQuantity: Int,
    isUpdating: Boolean,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPeachPink)
            .padding(16.dp)
    ) {
        // Summary items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Số lượng sản phẩm:", fontSize = 14.sp, color = Color.Gray)
            Text(totalQuantity.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tổng tiền:", fontSize = 14.sp, color = Color.Gray)
            Text(
                "${totalPrice.toFormattedPrice()} đ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PeachPinkAccent
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Checkout button
        Button(
            onClick = onCheckout,
            enabled = !isUpdating && totalQuantity > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
        ) {
            Text(
                text = if (isUpdating) "Đang cập nhật..." else "Thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Helper function to format price
fun Long.toFormattedPrice(): String {
    return String.format(java.util.Locale.getDefault(), "%,d", this)
}


package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.datn_mobile.domain.model.Product
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel
import java.util.Locale
import com.example.datn_mobile.utils.MessageManager

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit
) {
    val homeState = viewModel.homeState.collectAsState()
    val state = homeState.value

    // Show error message when error occurs
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Text(
            text = "Cửa hàng",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Loading state
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        // Empty state
        if (state.products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "📭",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Không có sản phẩm nào",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Vui lòng quay lại sau",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Tải lại")
                    }
                }
            }
            return@Column
        }

        // Products list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.products) { product ->
                ProductCard(
                    product = product,
                    onProductClick = onProductClick,
                    onAddToCartClick = onAddToCartClick
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit,
    isAuthenticated: Boolean = true  // ✅ Thêm parameter
) {
    // ✅ State để quản lý số lượng
    var quantity by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product image
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Product name
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Show color if available, otherwise show createDate
                    product.variant.color?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    } ?: run {
                        Text(
                            text = product.createDate,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // Show final price or regular price
                    val displayPrice = product.variant.finalPrice ?: product.variant.price ?: 0L
                    Text(
                        text = "${String.format(Locale.US, "%,d", displayPrice)} đ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    // Show original price if different from final price
                    if (product.variant.originalPrice != null &&
                        product.variant.originalPrice != product.variant.finalPrice) {
                        Text(
                            text = "${String.format(Locale.US, "%,d", product.variant.originalPrice)} đ",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // ✅ Quantity Control (+ / -)
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Minus button
                    IconButton(
                        onClick = {
                            if (quantity > 0) {
                                quantity--
                            }
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,  // ✅ Sử dụng Close icon
                            contentDescription = "Giảm",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Quantity display
                    Surface(
                        modifier = Modifier
                            .width(32.dp)
                            .fillMaxHeight(),
                        color = Color.LightGray.copy(alpha = 0.5f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "$quantity",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Plus button
                    IconButton(
                        onClick = {
                            // ✅ Kiểm tra số lượng có sẵn
                            val maxQuantity = product.variant.quantity ?: 0
                            if (quantity < maxQuantity) {
                                quantity++
                            } else if (maxQuantity > 0) {
                                MessageManager.showError("❌ Chỉ còn $maxQuantity sản phẩm")
                            } else {
                                MessageManager.showError("❌ Sản phẩm hết hàng")
                            }
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .padding(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tăng",
                            tint = Color.Green,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Add to cart button (khi quantity > 0)
                    if (quantity > 0) {
                        Button(
                            onClick = {
                                // ✅ Thêm vào giỏ hàng
                                if (isAuthenticated) {
                                    onAddToCartClick(product.id)
                                    MessageManager.showSuccess("✅ Đã thêm $quantity sản phẩm vào giỏ")
                                    quantity = 0  // Reset quantity
                                } else {
                                    MessageManager.showError("❌ Vui lòng đăng nhập để thêm vào giỏ hàng")
                                }
                            },
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Thêm", fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stock info - only show if quantity is available
            product.variant.quantity?.let {
                Text(
                    text = "Còn: $it",
                    fontSize = 12.sp,
                    color = if (it > 0) Color.Green else Color.Red
                )
            }
        }
    }
}




package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit
) {
    val products = viewModel.products.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

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
        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        // Error state
        error.value?.let { errorMsg ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Lỗi: $errorMsg", color = Color.Red)
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Thử lại")
                    }
                }
            }
            return@Column
        }

        // Empty state
        if (products.value.isEmpty()) {
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
            items(products.value) { product ->
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
    onAddToCartClick: (String) -> Unit
) {
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
                Column {
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

                Button(
                    onClick = { onAddToCartClick(product.id) },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Thêm giỏ")
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


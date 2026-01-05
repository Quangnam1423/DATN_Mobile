package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.theme.LightGray
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel
import java.util.Locale
import com.example.datn_mobile.utils.MessageManager

/**
 * Home Screen - Hiển thị danh sách sản phẩm theo hướng dẫn PRODUCT_API_GUIDELINE
 *
 * Luồng:
 * 1. Lấy danh sách sản phẩm từ GET /home
 * 2. Hiển thị dạng grid/list
 * 3. User click vào sản phẩm -> điều hướng tới ProductDetailScreen
 * 4. Trong ProductDetailScreen user chọn variant/attributes -> thêm vào giỏ
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit
) {
    val homeState = viewModel.homeState.collectAsState()
    val state = homeState.value

    // Hiển thị lỗi khi có
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Header
        Text(
            text = "Cửa hàng",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PeachPinkAccent,
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
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { viewModel.loadProducts() },
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
                    ) {
                        Text("Tải lại")
                    }
                }
            }
            return@Column
        }

        // Products list - Danh sách sản phẩm
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.products) { product ->
                ProductCard(
                    product = product,
                    onProductClick = onProductClick
                )
            }
        }
    }
}

/**
 * ProductCard - Thẻ sản phẩm cho Home Page
 *
 * Hiển thị:
 * - Ảnh sản phẩm (image)
 * - Tên sản phẩm (name)
 * - Giá bán (finalPrice)
 * - Giá gốc (originalPrice) nếu có giảm giá
 * - Ngày thêm (createDate)
 * - Nút "Xem Chi Tiết" để điều hướng tới ProductDetailScreen
 *
 * Không hiển thị:
 * - Chọn variant/color
 * - Chọn attributes (size, loại)
 * - Chọn số lượng
 * - Nút "Thêm vào giỏ"
 *
 * Theo hướng dẫn: Người dùng phải vào trang chi tiết để chọn variant/attributes
 * rồi mới có thể thêm vào giỏ hàng
 */
@Composable
fun ProductCard(
    product: Product,
    onProductClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 1. Ảnh sản phẩm (image)
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Tên sản phẩm (name)
            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Giá sản phẩm
            Column {
                // Giá bán (finalPrice)
                val displayPrice = ((product.variant.finalPrice
                    ?: product.variant.price
                    ?: 0.0)).toLong()
                Text(
                    text = "${String.format(Locale.US, "%,d", displayPrice)} đ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PeachPinkAccent
                )

                // Giá gốc (originalPrice) - chỉ hiển thị nếu có giảm giá
                if (product.variant.originalPrice != null &&
                    product.variant.originalPrice != product.variant.finalPrice) {

                    Spacer(modifier = Modifier.height(4.dp))

                    val originalPrice = (product.variant.originalPrice ?: 0.0).toLong()
                    Text(
                        text = "${String.format(Locale.US, "%,d", originalPrice)} đ",
                        fontSize = 11.sp,
                        color = Color.Black,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Nút "Xem Chi Tiết" - điều hướng tới ProductDetailScreen
            Button(
                onClick = { onProductClick(product.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
            ) {
                Text(
                    text = "Xem Chi Tiết",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Ngày thêm sản phẩm (createDate)
            // Text(
            //     text = "Ngày thêm: ${product.createDate}",
            //     fontSize = 12.sp,
            //     color = Color.Black
            // )
        }
    }
}

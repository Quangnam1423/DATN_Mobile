package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.datn_mobile.domain.model.ProductDetail
import com.example.datn_mobile.domain.model.ProductDetailVariant
import com.example.datn_mobile.domain.model.ProductDetailVariantAttribute
import com.example.datn_mobile.presentation.viewmodel.ProductDetailViewModel
import com.example.datn_mobile.utils.MessageManager
import java.util.*

/**
 * ProductDetailScreen - Chi tiết sản phẩm theo hướng dẫn PRODUCT_API_GUIDELINE
 *
 * Luồng:
 * 1. Nhận productId từ navigation
 * 2. Gọi GET /home/product/{productId}
 * 3. Hiển thị:
 *    - Ảnh đại diện (image)
 *    - Ảnh giới thiệu (introImages)
 *    - Tên sản phẩm
 *    - Danh sách variants (màu sắc)
 *    - Ảnh chi tiết theo variant
 *    - Danh sách attributes (size) theo variant
 *    - Giá bán & giá gốc
 * 4. User chọn:
 *    - Variant (màu sắc)
 *    - Attribute (size)
 * 5. User click "Thêm vào giỏ"
 *    - Gọi POST /cart/add/{attId}
 *    - attId lấy từ attribute được chọn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit,
) {
    val detailState = viewModel.productDetailState.collectAsState()
    val state = detailState.value

    // Load product detail khi vào screen
    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }

    // Hiển thị lỗi khi có
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    // Hiển thị thông báo thêm vào giỏ hàng
    LaunchedEffect(state.addToCartSuccess) {
        if (state.addToCartSuccess) {
            MessageManager.showSuccess("Thêm vào giỏ hàng thành công")
        }
    }

    LaunchedEffect(state.addToCartError) {
        state.addToCartError?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header với nút back
        TopAppBar(
            title = { Text("Chi tiết sản phẩm") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
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

        // Product detail
        val product = state.product
        if (product != null) {
            ProductDetailContent(
                product = product,
                onAddToCartClick = { attId ->
                    viewModel.addToCart(attId)
                },
                isAddingToCart = state.isAddingToCart
            )
        } else {
            // Error state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "❌",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Không tìm thấy sản phẩm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = onBackClick) {
                        Text("Quay lại")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: ProductDetail,
    onAddToCartClick: (String) -> Unit,
    isAddingToCart: Boolean
) {
    // State để quản lý variant được chọn
    var selectedVariantIndex by remember { mutableStateOf(0) }
    var selectedAttribute by remember { mutableStateOf<ProductDetailVariantAttribute?>(null) }
    var mainImageUrl by remember { mutableStateOf(product.image) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 1. Ảnh sản phẩm chính
        item {
            Column {
                AsyncImage(
                    model = mainImageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Thumbnail list - Ảnh giới thiệu
                if (!product.introImages.isNullOrEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(product.introImages) { image ->
                            AsyncImage(
                                model = image.url,
                                contentDescription = "intro",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable { mainImageUrl = image.url },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        // 2. Tên sản phẩm
        item {
            Text(
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 3. Chọn Variant (Màu sắc)
        if (!product.variants.isNullOrEmpty()) {
            item {
                Column {
                    Text(
                        text = "Chọn Màu Sắc",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(product.variants.size) { index ->
                            VariantButton(
                                variant = product.variants[index],
                                isSelected = selectedVariantIndex == index,
                                onClick = {
                                    selectedVariantIndex = index
                                    selectedAttribute = null // Reset selected attribute
                                    mainImageUrl = product.image // Reset to main image
                                }
                            )
                        }
                    }
                }
            }

            // 4. Ảnh chi tiết của variant đã chọn
            val currentVariant = product.variants[selectedVariantIndex]
            if (!currentVariant.detailImages.isNullOrEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Ảnh chi tiết",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(currentVariant.detailImages) { image ->
                                AsyncImage(
                                    model = image.url,
                                    contentDescription = "detail",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clickable { mainImageUrl = image.url },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            // 5. Chọn Attributes (Size / Loại)
            if (!currentVariant.attributes.isNullOrEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Chọn Size",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            currentVariant.attributes.forEach { attribute ->
                                AttributeButton(
                                    attribute = attribute,
                                    isSelected = selectedAttribute?.id == attribute.id,
                                    onClick = { selectedAttribute = attribute }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Giá sản phẩm
        item {
            if (selectedAttribute != null) {
                val attr = selectedAttribute!!
                Column {
                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            // Giá bán
                            Text(
                                text = "${String.format(Locale.US, "%,d", attr.finalPrice ?: 0)} đ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )

                            // Giá gốc
                            if (attr.originalPrice != null && attr.originalPrice != attr.finalPrice) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${String.format(Locale.US, "%,d", attr.originalPrice)} đ",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }

                            // Phần trăm giảm
                            if (attr.discount != null && attr.discount > 0) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Giảm ${attr.discount.toInt()}%",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFF6B6B),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider()
                }
            }
        }

        // 7. Nút "Thêm vào giỏ hàng"
        item {
            if (selectedAttribute != null) {
                Button(
                    onClick = {
                        onAddToCartClick(selectedAttribute!!.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)),
                    enabled = !isAddingToCart
                ) {
                    if (isAddingToCart) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(
                            text = "🛒 Thêm vào giỏ hàng",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(
                        text = "Vui lòng chọn Size",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun VariantButton(
    variant: ProductDetailVariant,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF6200EA) else Color.LightGray,
            contentColor = if (isSelected) Color.White else Color.Black
        )
    ) {
        Text(
            text = variant.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AttributeButton(
    attribute: ProductDetailVariantAttribute,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color(0xFF6200EA).copy(alpha = 0.1f) else Color.White,
            contentColor = if (isSelected) Color(0xFF6200EA) else Color.Black
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF6200EA) else Color.Gray
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = attribute.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${String.format(Locale.US, "%,d", attribute.finalPrice ?: 0)} đ",
                fontSize = 12.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

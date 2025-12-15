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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit,
) {
    val detailState = viewModel.productDetailState.collectAsState()
    val state = detailState.value

    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }

    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

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
        TopAppBar(
            title = { Text("Chi tiết sản phẩm") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, "Back")
                }
            }
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

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
    var selectedVariantIndex by remember { mutableStateOf(0) }
    var selectedAttribute by remember { mutableStateOf<ProductDetailVariantAttribute?>(null) }
    var mainImageUrl by remember { mutableStateOf(product.image) }

    Scaffold(
        bottomBar = {
            AddToCartSection(
                selectedAttribute = selectedAttribute,
                isAddingToCart = isAddingToCart,
                onAddToCartClick = onAddToCartClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
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

            item {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

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
                                        selectedAttribute = null
                                        mainImageUrl = product.image
                                    }
                                )
                            }
                        }
                    }
                }

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

                if (currentVariant.attributes.isNullOrEmpty()) {
                    item {
                        Text(
                            text = "Phiên bản này hiện chưa có sẵn để mua.",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                } else {
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
                                        Text(
                                            text = "${String.format(Locale.US, "%,d", attr.finalPrice ?: 0)} đ",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Red
                                        )

                                        if (attr.originalPrice != null && attr.originalPrice != attr.finalPrice) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${String.format(Locale.US, "%,d", attr.originalPrice)} đ",
                                                fontSize = 14.sp,
                                                color = Color.Gray,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }

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
                }
            }
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
            .heightIn(min = 56.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(
                text = attribute.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${String.format(Locale.US, "%,d", attribute.finalPrice ?: 0)} đ",
                fontSize = 13.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddToCartSection(
    selectedAttribute: ProductDetailVariantAttribute?,
    isAddingToCart: Boolean,
    onAddToCartClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.navigationBarsPadding(),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (selectedAttribute != null) {
                Button(
                    onClick = { onAddToCartClick(selectedAttribute.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
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
                        .height(52.dp),
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
        }
    }
}

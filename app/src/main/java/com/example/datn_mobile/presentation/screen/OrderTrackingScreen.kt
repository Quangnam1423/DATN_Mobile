package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.viewmodel.OrderTrackingViewModel
import com.example.datn_mobile.domain.model.PaymentStatus
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderTrackingScreen(
    viewModel: OrderTrackingViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onOrderClick: (Order) -> Unit = {}
) {
    val state by viewModel.orderTrackingState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Đơn mua, 1 = Đơn sửa

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            Text(
                text = "Theo dõi đơn hàng",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PeachPinkAccent,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = PeachPinkAccent,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Text(
                        "Đơn mua",
                        color = if (selectedTabIndex == 0) PeachPinkAccent else Color.Gray,
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Text(
                        "Đơn sửa",
                        color = if (selectedTabIndex == 1) PeachPinkAccent else Color.Gray,
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PeachPinkAccent)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = state.error ?: "Có lỗi xảy ra",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { viewModel.refreshOrders() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PeachPinkAccent,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Thử lại")
                        }
                    }
                }
            }

            else -> {
                val filteredOrders = state.orders.filter { order ->
                    if (selectedTabIndex == 0) {
                        order.type == 0 // Đơn mua
                    } else {
                        order.type == 1 // Đơn sửa
                    }
                }.sortedWith(compareByDescending<Order> { order ->
                    // Ưu tiên updatedAt nếu có, nếu không thì dùng orderAt
                    order.updatedAt ?: order.orderAt
                })

                if (filteredOrders.isEmpty()) {
                    EmptyOrdersView(selectedTabIndex == 1)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders) { order ->
                            val paymentStatus = viewModel.getPaymentStatus(order.id)
                            OrderCard(
                                order = order,
                                paymentStatusText = paymentStatus?.let { mapPaymentStatusToText(it) },
                                onClick = { onOrderClick(order) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    paymentStatusText: String? = null,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightPeachPink
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mã đơn: ${order.id.take(8)}...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PeachPinkAccent
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (order.status) {
                        0 -> Color(0xFFFF9800) // Orange for pending
                        1 -> Color(0xFF4CAF50) // Green for confirmed
                        2 -> Color(0xFF2196F3) // Blue for processing
                        3 -> Color(0xFF9C27B0) // Purple for completed
                        else -> Color.Gray
                    }
                ) {
                    Text(
                        text = when (order.status) {
                            0 -> "Chờ xử lý"
                            1 -> "Đã xác nhận"
                            2 -> "Đang xử lý"
                            3 -> "Hoàn thành"
                            else -> "Không xác định"
                        },
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Payment status (nếu có)
            if (!paymentStatusText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Thanh toán: $paymentStatusText",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Order Date
            Text(
                text = "Ngày đặt: ${order.orderAt}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            if (!order.description.isNullOrBlank()) {
                Text(
                    text = "Mô tả: ${order.description}",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Order Items
            if (order.orderItems.isNotEmpty()) {
                Text(
                    text = "Sản phẩm:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                order.orderItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.img,
                            contentDescription = item.productName,
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.productName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${item.productAttName} - ${item.color}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Số lượng: ${item.quantity}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = formatPrice(item.price.toLong()),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PeachPinkAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total Price
            if (order.type == 0 && order.totalPrice > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tổng tiền:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatPrice(order.totalPrice.toLong()),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PeachPinkAccent
                    )
                }
            }

            // Contact Info
            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Column {
                Text(
                    text = "Thông tin liên hệ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Tên: ${order.userName}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "SĐT: ${order.phoneNumber}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Email: ${order.email}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (!order.address.isNullOrBlank()) {
                    Text(
                        text = "Địa chỉ: ${order.address}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyOrdersView(isRepairOrder: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📦",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = if (isRepairOrder) "Chưa có đơn sửa chữa nào" else "Chưa có đơn mua nào",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = if (isRepairOrder) {
                    "Bạn chưa có đơn sửa chữa nào. Hãy tạo đơn sửa chữa mới!"
                } else {
                    "Bạn chưa có đơn mua nào. Hãy mua sắm ngay!"
                },
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatPrice(price: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    return "${formatter.format(price)} đ"
}

private fun mapPaymentStatusToText(status: PaymentStatus): String {
    // Có thể tùy chỉnh mapping theo backend, tạm thời:
    return when (status.status) {
        1 -> "Đã thanh toán"
        0 -> "Đang chờ thanh toán"
        else -> status.statusText ?: "Trạng thái không xác định"
    }
}


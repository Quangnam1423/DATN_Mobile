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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.viewmodel.CartViewModel

@Composable
fun CheckoutScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onOrderSuccess: () -> Unit
    ) {
    val cartState by viewModel.cartState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val totalPrice = cartState.totalPrice
    val totalQuantity = cartState.totalQuantity
    
    // Lấy danh sách sản phẩm đã chọn
    val selectedItems = cartState.cart?.items?.filter { 
        it.id in cartState.selectedItemIds 
    } ?: emptyList()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        // Header (không scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            Text(
                text = "Thanh toán",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        if (totalQuantity == 0 || totalPrice == 0L) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vui lòng quay lại giỏ hàng và chọn sản phẩm để thanh toán",
                    textAlign = TextAlign.Center
                )
            }
            return@Column
        }

        // Nội dung scroll được: form + tóm tắt đơn hàng
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // Thông tin đơn hàng
            Text(
                text = "Thông tin nhận hàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Họ và tên") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
            )

            OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            )

            OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Địa chỉ giao hàng") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
            )

            OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Ghi chú (không bắt buộc)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            maxLines = 3
            )

            // Tóm tắt đơn hàng
            Text(
                text = "Tóm tắt đơn hàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Danh sách sản phẩm đã chọn
            selectedItems.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Tên sản phẩm
                    Text(
                        text = item.productName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    // Phân loại (GB, màu, size...)
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
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
                    
                    // Số lượng và giá
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Số lượng: ${item.quantity}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
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

            Spacer(modifier = Modifier.height(8.dp))

            // Tổng số lượng và tổng tiền
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Số lượng sản phẩm:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = totalQuantity.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng tiền:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${totalPrice.toFormattedPrice()} đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PeachPinkAccent
                )
            }
        }

        Button(
            onClick = {
                viewModel.placeOrder(
                    type = 0,
                    phoneNumber = phoneNumber,
                    email = email,
                    address = address,
                    description = description.ifBlank { null }
                )
                onOrderSuccess()
            },
            enabled = !cartState.isUpdating && totalQuantity > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
        ) {
            Text(
                text = if (cartState.isUpdating) "Đang xử lý..." else "Mua hàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



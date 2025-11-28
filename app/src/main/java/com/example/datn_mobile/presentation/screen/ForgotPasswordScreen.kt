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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.compose.DATN_MobileTheme

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onResetClick: (String) -> Unit = {}
) {
    ForgotPasswordContent(
        onBackClick = onBackClick,
        onResetClick = onResetClick
    )
}

@Composable
fun ForgotPasswordContent(
    onBackClick: () -> Unit = {},
    onResetClick: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            // Back button on the left
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            // Title centered
            Text(
                text = "Quên Mật Khẩu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PeachPinkAccent,
                modifier = Modifier.align(Alignment.Center),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Form content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Instructions
            Text(
                text = "Nhập lại thông tin liên quan để nhận hướng dẫn đặt lại mật khẩu",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Email field label
            Text(
                text = "Email hoặc Số điện thoại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LightPeachPink,
                    unfocusedContainerColor = LightPeachPink,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp
                ),
                placeholder = {
                    Text(
                        text = "Vui lòng điền vào đây",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Reset button
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        isLoading = true
                        errorMessage = null
                        successMessage = null
                        onResetClick(email)
                        // Simulate loading
                        isLoading = false
                        successMessage = "Đã gửi hướng dẫn đặt lại mật khẩu đến email của bạn"
                    } else {
                        errorMessage = "Vui lòng nhập email hoặc tên đăng nhập"
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PeachPinkAccent,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Gửi Hướng Dẫn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Messages
            successMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }

            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Back to login link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.TextButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Quay lại Đăng Nhập",
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "default status")
@Composable
fun ForgotPasswordPreview() {
    DATN_MobileTheme {
        ForgotPasswordContent(
            onBackClick = {},
            onResetClick = {}
        )
    }
}


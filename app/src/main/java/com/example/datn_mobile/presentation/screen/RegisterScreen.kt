package com.example.datn_mobile.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.viewmodel.RegisterState
import com.example.datn_mobile.presentation.viewmodel.RegisterViewModel
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.compose.DATN_MobileTheme


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isRegisterSuccess, state.errorMessage) {
        if (state.isRegisterSuccess) {
            Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
        if (state.errorMessage != null) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    RegisterContent(
        state = state,
        onRegisterClicked = { fullName, email, phoneNumber, password, confirmPass ->
            viewModel.onRegisterClicked(fullName, email, phoneNumber, password, confirmPass)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RegisterContent(
    state: RegisterState,
    onRegisterClicked: (String, String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                onClick = onNavigateBack,
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
                text = "Tạo Tài Khoản",
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

            // Full Name field
            Text(
                text = "Họ Và Tên",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                        text = "Nguyễn Văn A",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email field
            Text(
                text = "Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        text = "example@gmail.com",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Phone number field
            Text(
                text = "Số Điện Thoại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                        text = "0123456789",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password field
            Text(
                text = "Mật Khẩu",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        text = "Vui lòng nhập mật ",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password field
            Text(
                text = "Nhập Lại Mật Khẩu",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        text = "Nhập xác nhận lại mật khẩu",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Terms and conditions text
            Text(
                text = "Bằng việc tiếp tục, bạn sẽ đồng ý với các điều khoản của ứng dụng",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register button
            Button(
                onClick = {
                    onRegisterClicked(fullName, email, phoneNumber, password, confirmPassword)
                },
                enabled = !state.isLoading,
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
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Đăng Ký",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                androidx.compose.material3.TextButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Đăng nhập",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PeachPinkAccent
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Trạng thái mặc định")
@Composable
fun RegisterScreenPreview() {
    DATN_MobileTheme {
        RegisterContent(
            state = RegisterState(isLoading = false, errorMessage = null),
            onRegisterClicked = { _, _, _, _, _ -> },
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Trạng thái Đang tải")
@Composable
fun RegisterScreenLoadingPreview() {
    DATN_MobileTheme {
        RegisterContent(
            state = RegisterState(isLoading = true, errorMessage = null),
            onRegisterClicked = { _, _, _, _, _ -> },
            onNavigateBack = {}
        )
    }
}
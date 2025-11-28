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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.viewmodel.LoginState
import com.example.datn_mobile.presentation.viewmodel.LoginViewModel
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.compose.DATN_MobileTheme

@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    val savedPhoneNumber by viewModel.savedPhoneNumberFlow.collectAsState(initial = null)

    // handle "side effect" like navigate
    LaunchedEffect(loginState.isLoginSuccess) {
        if (loginState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    LoginContent(
        state = loginState,
        savedPhoneNumber = savedPhoneNumber,
        onLoginClicked = {phoneNumber, password ->
            viewModel.onLoginClicked(phoneNumber, password)
        },
        onBackClick = onBackClick,
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}

@Composable
fun LoginContent (
    state: LoginState,
    savedPhoneNumber: String? = null,
    onLoginClicked: (String, String) -> Unit,
    onBackClick: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    // State of UI for OutlinedTextField - pre-fill with saved phone number
    var phoneNumber by remember { mutableStateOf(savedPhoneNumber ?: "")}
    var password by remember {mutableStateOf("")}

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
                text = "Đăng Nhập",
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

            // Welcome message
            Text(
                text = "Rất Vui Gặp Lại Bạn",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Vui lòng điền các thông tin bên dưới để tiếp tục",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

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
                onValueChange = {phoneNumber = it},
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
                )
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
                onValueChange = {password = it},
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
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot password link
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                androidx.compose.material3.TextButton(
                    onClick = onNavigateToForgotPassword,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Quên mật khẩu?",
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onLoginClicked(phoneNumber, password)
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
                Text(
                    text = "Đăng Nhập",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = PeachPinkAccent
                )
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

            // Register section
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chưa có tài khoản? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                androidx.compose.material3.TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Đăng ký",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "default status")
@Composable
fun LoginContentPreview () {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = false, errorMessage = null),
            onLoginClicked = {_, _ ->},
            onBackClick = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}

@Preview(showBackground = true, name = "loading status")
@Composable
fun LoginScreenLoadingPreview() {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = true, errorMessage = null),
            onLoginClicked = {_, _ ->},
            onBackClick = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}

@Preview(showBackground = true, name = "error status")
@Composable
fun LoginScreenErrorPreview() {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = false,
                errorMessage = "Số điện thoại hoặc mật khẩu sai"
            ),
            onLoginClicked = {_, _ ->},
            onBackClick = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}

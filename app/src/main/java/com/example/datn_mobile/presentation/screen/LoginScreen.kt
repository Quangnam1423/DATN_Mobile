package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.datn_mobile.presentation.viewmodel.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.viewmodel.LoginState
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

    // handle "side effect" like navigate
    LaunchedEffect(loginState.isLoginSuccess) {
        if (loginState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    LoginContent(
        state = loginState,
        onLoginClicked = {email, password ->
            viewModel.onLoginClicked(email, password)
        },
        onBackClick = onBackClick,
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}

@Composable
fun LoginContent (
    state: LoginState,
    onLoginClicked: (String, String) -> Unit,
    onBackClick: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    // State of UI for OutlinedTextField
    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.Black
                )
            }

            Text(
                text = "Đăng Nhập",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        // Form content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(0.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Tên Đăng Nhập")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Mật Khẩu")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot password link
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            androidx.compose.material3.TextButton(
                onClick = onNavigateToForgotPassword
            ) {
                Text(
                    text = "Quên mật khẩu?",
                    fontSize = 12.sp,
                    color = Color(0xFF2196F3)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onLoginClicked(email, password)
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Đăng Nhập")
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        if (state.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.errorMessage,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register section
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chưa có tài khoản?",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            androidx.compose.material3.TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Đăng ký ngay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
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
                errorMessage = "Tên đăng nhập hoặc mật khẩu sai"
            ),
            onLoginClicked = {_, _ ->},
            onBackClick = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}

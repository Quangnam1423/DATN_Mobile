package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datn_mobile.presentation.viewmodel.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.viewmodel.LoginState
import com.example.compose.DATN_MobileTheme

@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(), // tự khởi tạo rồi truyenf vào.
    onNavigateToHome: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNetworkError: () -> Unit = {},
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
        }
    )
}

@Composable
fun LoginContent (
    state: LoginState,
    onLoginClicked: (String, String) -> Unit
) {
    // State of UI for OutlinedTextField
    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Đăng Nhập", color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Tên Đăng Nhập")},
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Mật Khẩu")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onLoginClicked(email, password)
            },
            enabled  = !state.isLoading
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
    }
}

@Preview(showBackground = true, name = "default status")
@Composable
fun LoginContentPreview () {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = false, errorMessage = null),
            onLoginClicked = {_, _ ->} // DO NO THING
        )
    }
}

@Preview(showBackground = true, name = "loading status")
@Composable
fun LoginScreenLoadingPreview() {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = true, errorMessage = null),
            onLoginClicked = {_, _ ->} // DO NO THING
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
            onLoginClicked = {_, _ ->} // DO NO THING
        )
    }
}

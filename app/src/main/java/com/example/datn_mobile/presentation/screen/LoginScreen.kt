package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

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
        onLoginClicked = { phoneNumber, password ->
            // Use phoneNumber as email for now (backend might need adjustment)
            viewModel.onLoginClicked(phoneNumber, password)
        },
        onNavigateBack = onNavigateToRegister
    )
}

@Composable
fun LoginContent (
    state: LoginState,
    onLoginClicked: (String, String) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    // State of UI for OutlinedTextField
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Colors matching the design
    val white = Color.White
    val lightBlue = Color(0xFF57A3DE) // Light blue for clouds
    val darkGrey = Color(0xFF0C0C0C) // Dark grey for text
    val lightGrey = Color(0xFF867F7F) // Light grey for placeholders and buttons
    val lightGreyButton = Color(0xFFA29E9E) // Light grey for disabled button
    val black = Color.Black // For heart icon

    // Check if form is valid
    val isFormValid = phoneNumber.isNotBlank() && password.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        // Cloud shapes in background - Top section
        // Top left cloud (large)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(100.dp)
                .offset(x = (-30).dp, y = (-20).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.5f))
        )
        
        // Top left small cloud
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(60.dp)
                .offset(x = 20.dp, y = 40.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        // Top right cloud (large)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(120.dp)
                .offset(x = 40.dp, y = (-30).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        // Top right medium cloud
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(80.dp)
                .offset(x = (-20).dp, y = 60.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        // Top center small cloud
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(70.dp)
                .offset(y = 30.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        // Middle left cloud
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(90.dp)
                .offset(x = (-40).dp, y = (-50).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        // Middle right small cloud
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(65.dp)
                .offset(x = 30.dp, y = (-80).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        // Bottom left medium cloud
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(110.dp)
                .offset(x = (-25).dp, y = 150.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        // Bottom right small cloud
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(75.dp)
                .offset(x = 35.dp, y = 120.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        // Large bottom cloud (center)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(300.dp)
                .offset(y = 100.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        // Bottom center small cloud (overlapping)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(85.dp)
                .offset(x = 60.dp, y = 80.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        // Another small cloud near bottom center
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(70.dp)
                .offset(x = (-80).dp, y = 60.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Title "Login"
            Text(
                text = "Login",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = darkGrey
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Welcome message with heart icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Good to see you back!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = lightGrey
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Heart",
                    tint = black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Phone Number field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Phone Number", color = lightGrey) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5), // Very light grey
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = lightGrey) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Next button
            Button(
                onClick = {
                    onLoginClicked(phoneNumber, password)
                },
                enabled = isFormValid && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid && !state.isLoading) lightGrey else lightGreyButton,
                    contentColor = if (isFormValid && !state.isLoading) darkGrey else lightGrey
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = darkGrey
                    )
                } else {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isFormValid) darkGrey else lightGrey
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

            Spacer(modifier = Modifier.weight(1f))

            // Cancel link at bottom
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = lightGrey
                )

            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    color = lightGrey,
                    fontWeight = FontWeight.Normal
                )
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
            onLoginClicked = {_, _ ->}, // DO NO THING
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "loading status")
@Composable
fun LoginScreenLoadingPreview() {
    DATN_MobileTheme {
        LoginContent(
            state = LoginState(isLoading = true, errorMessage = null),
            onLoginClicked = {_, _ ->}, // DO NO THING
            onNavigateBack = {}
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
            onLoginClicked = {_, _ ->}, // DO NO THING
            onNavigateBack = {}
        )
    }
}

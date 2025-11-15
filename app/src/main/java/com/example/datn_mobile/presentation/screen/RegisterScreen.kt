package com.example.datn_mobile.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.presentation.viewmodel.RegisterState
import com.example.datn_mobile.presentation.viewmodel.RegisterViewModel
import com.example.datn_mobile.ui.theme.DATN_MobileTheme


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
        onRegisterClicked = { email, password, confirmPass ->
            viewModel.onRegisterClicked(email, password, confirmPass)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RegisterContent(
    state: RegisterState,
    onRegisterClicked: (String, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Colors matching the design
    val lightBlue = Color(0xFFB4D3EA) // Light blue background
    val white = Color.White
    val darkGrey = Color(0xFF0A0A0A) // Dark grey for text
    val lightGrey = Color(0xFF858282) // Light grey for buttons and placeholders
    val lightGreyButton = Color(0xFFE0E0E0) // Light grey for disabled button

    // Check if all fields are filled
    val isFormValid = fullName.isNotBlank() && 
                     email.isNotBlank() && 
                     number.isNotBlank() && 
                     password.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        // Light blue cloud shapes in background - distributed throughout the screen
        // Top section clouds
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(90.dp)
                .offset(x = (-35).dp, y = 50.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(110.dp)
                .offset(x = 45.dp, y = 40.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(75.dp)
                .offset(y = 80.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        // Middle section clouds
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(100.dp)
                .offset(x = (-45).dp, y = (-30).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(85.dp)
                .offset(x = 40.dp, y = (-50).dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(70.dp)
                .offset(y = 20.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        // Bottom section clouds
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(120.dp)
                .offset(x = (-30).dp, y = 100.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(95.dp)
                .offset(x = 50.dp, y = 80.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(250.dp)
                .offset(y = 120.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.3f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(80.dp)
                .offset(x = 70.dp, y = 90.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.4f))
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(65.dp)
                .offset(x = (-90).dp, y = 70.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.35f))
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with light blue background and white shapes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(lightBlue)
            ) {
                // White cloud/wave-like shapes
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(120.dp)
                        .offset(x = (-40).dp, y = (-30).dp)
                        .clip(CircleShape)
                        .background(white.copy(alpha = 0.6f))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(100.dp)
                        .offset(x = 30.dp, y = (-20).dp)
                        .clip(CircleShape)
                        .background(white.copy(alpha = 0.5f))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(80.dp)
                        .offset(x = 60.dp, y = 40.dp)
                        .clip(CircleShape)
                        .background(white.copy(alpha = 0.4f))
                )
                
                // Additional white shapes in header
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(70.dp)
                        .offset(y = 10.dp)
                        .clip(CircleShape)
                        .background(white.copy(alpha = 0.3f))
                )
                
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(60.dp)
                        .offset(x = (-50).dp, y = 50.dp)
                        .clip(CircleShape)
                        .background(white.copy(alpha = 0.35f))
                )
                
                // Title "Create Account"
                Text(
                    text = "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGrey,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 24.dp)
                )
            }

            // Input fields section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Full name field
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Full name", color = lightGrey) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = white,
                        focusedContainerColor = white
                    )
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email", color = lightGrey) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = white,
                        focusedContainerColor = white
                    )
                )

                // Number field
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    placeholder = { Text("Number", color = lightGrey) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = white,
                        focusedContainerColor = white
                    )
                )

                // Password field with visibility toggle
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = lightGrey) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = lightGrey
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = white,
                        focusedContainerColor = white
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Done button
                Button(
                    onClick = {
                        // Use password as confirmPassword for now since we removed that field
                        onRegisterClicked(email, password, password)
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
                            text = "Done",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isFormValid) darkGrey else lightGrey
                        )
                    }
                }
            }

            // Cancel link at bottom
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
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

@Preview(showBackground = true, name = "Trạng thái mặc định")
@Composable
fun RegisterScreenPreview() {
    DATN_MobileTheme {
        RegisterContent(
            state = RegisterState(isLoading = false, errorMessage = null),
            onRegisterClicked = { _, _, _ -> },
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
            onRegisterClicked = { _, _, _ -> },
            onNavigateBack = {}
        )
    }
}
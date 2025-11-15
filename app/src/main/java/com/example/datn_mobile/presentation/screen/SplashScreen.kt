package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    SplashContent(
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun SplashContent(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    // Colors matching the design
    val whiteBackground = Color.White
    val blueButton = Color(0xFF2196F3) // Vibrant blue
    val darkGrey = Color(0xFF424242) // Dark grey for text
    val lightGrey = Color(0xFF9E9E9E) // Light grey for subtitle
    val iconGrey = Color(0xFFE0E0E0) // Light grey for icon border
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top spacer
            Spacer(modifier = Modifier.height(120.dp))
            
            // Main content - Icon, App name and tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // App Icon - Shopping bag in circular border
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            width = 2.dp,
                            color = iconGrey,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = "App Icon",
                        tint = Color(0xFF64B5F6), // Light blue
                        modifier = Modifier.size(60.dp)
                    )
                }
                
                // App Name - Large, bold, dark grey
                Text(
                    text = "PTIT",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGrey,
                    letterSpacing = 2.sp
                )
                
                // Tagline - Two lines, smaller, light grey
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "An eCommerce Mobile App",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = lightGrey,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "for your online store",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = lightGrey,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Bottom - Buttons and login link
            Column(
                modifier = Modifier.padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // "Let's get started" Button
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blueButton
                    )
                ) {
                    Text(
                        text = "Let's get started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                
                // "I already have an account" with arrow
                Row(
                    modifier = Modifier
                        .clickable(onClick = onNavigateToLogin)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "I already have an account",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = darkGrey
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = blueButton,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Navigate to Login",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Splash Screen Preview")
@Composable
fun SplashContentPreview() {
    SplashContent()
}


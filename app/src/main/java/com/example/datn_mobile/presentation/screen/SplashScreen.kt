package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onTimeout()
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    // Purple background color (similar to the design)
    val purpleBackground = Color(0xFF6A4C93) // Medium-dark purple
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top spacer to push content down
            Spacer(modifier = Modifier.height(120.dp))
            
            // Main content - App name and tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // App Name - Large, bold, white
                Text(
                    text = "PTIT",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                
                // Tagline - Smaller white text
                Text(
                    text = "Ứng dụng di động tốt nghiệp",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
            }
            
            // Bottom - Version info
            Text(
                text = "Version 1.0.0",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Splash Screen Preview")
@Composable
fun SplashContentPreview() {
    SplashContent()
}


package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.presentation.viewmodel.AuthState
import com.example.datn_mobile.presentation.viewmodel.SplashViewModel

// Soft peach-pink background color
val SplashBackgroundColor = Color(0xFFFFE5D4)

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val authState = viewModel.authState.collectAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> onNavigateToHome()
            is AuthState.Unauthenticated -> onNavigateToLogin()
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // House and Sofa Icon
            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            ) {
                val width = size.width
                val height = size.height
                val centerX = width / 2
                val centerY = height / 2
                
                // House outline
                val housePath = Path().apply {
                    // Roof (triangle)
                    moveTo(centerX, centerY - height * 0.25f)
                    lineTo(centerX - width * 0.25f, centerY - height * 0.05f)
                    lineTo(centerX + width * 0.25f, centerY - height * 0.05f)
                    close()
                    
                    // Chimney on left side of roof
                    moveTo(centerX - width * 0.2f, centerY - height * 0.15f)
                    lineTo(centerX - width * 0.2f, centerY - height * 0.25f)
                    lineTo(centerX - width * 0.12f, centerY - height * 0.25f)
                    lineTo(centerX - width * 0.12f, centerY - height * 0.15f)
                    close()
                    
                    // House base (rectangle)
                    moveTo(centerX - width * 0.25f, centerY - height * 0.05f)
                    lineTo(centerX - width * 0.25f, centerY + height * 0.25f)
                    lineTo(centerX + width * 0.25f, centerY + height * 0.25f)
                    lineTo(centerX + width * 0.25f, centerY - height * 0.05f)
                    close()
                }
                
                // Sofa inside house
                val sofaPath = Path().apply {
                    val sofaWidth = width * 0.3f
                    val sofaHeight = height * 0.15f
                    val sofaX = centerX - sofaWidth / 2
                    val sofaY = centerY + height * 0.05f
                    
                    // Sofa backrest
                    moveTo(sofaX, sofaY)
                    lineTo(sofaX, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX + sofaWidth, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX + sofaWidth, sofaY)
                    
                    // Sofa seat
                    moveTo(sofaX, sofaY)
                    lineTo(sofaX + sofaWidth, sofaY)
                    lineTo(sofaX + sofaWidth, sofaY + sofaHeight * 0.3f)
                    lineTo(sofaX, sofaY + sofaHeight * 0.3f)
                    close()
                    
                    // Left armrest
                    moveTo(sofaX, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX - sofaWidth * 0.1f, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX - sofaWidth * 0.1f, sofaY + sofaHeight * 0.3f)
                    lineTo(sofaX, sofaY + sofaHeight * 0.3f)
                    
                    // Right armrest
                    moveTo(sofaX + sofaWidth, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX + sofaWidth + sofaWidth * 0.1f, sofaY - sofaHeight * 0.4f)
                    lineTo(sofaX + sofaWidth + sofaWidth * 0.1f, sofaY + sofaHeight * 0.3f)
                    lineTo(sofaX + sofaWidth, sofaY + sofaHeight * 0.3f)
                }
                
                // Draw house outline in white
                drawPath(
                    path = housePath,
                    color = Color.White,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                
                // Draw sofa outline in white
                drawPath(
                    path = sofaPath,
                    color = Color.White,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
            
            // PTIT text - bold, uppercase, white
            Text(
                text = "PTIT",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // ESHOPP text - smaller, lighter weight, uppercase, white with letter spacing
            Text(
                text = "ESHOPP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp
            )
        }
    }
}
package com.example.datn_mobile.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datn_mobile.R
import com.example.datn_mobile.ui.theme.DATN_MobileTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    onStartClick: () -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onTimeout()
    }

    SplashContent(
        onStartClick = onStartClick
    )
}

@Composable
fun SplashContent(
    onStartClick: () -> Unit = {},
    enableAnimation: Boolean = true
) {
    // Animation states - start with visible values if animation disabled (for preview)
    val scale = remember { Animatable(if (enableAnimation) 0f else 1f) }
    val alpha = remember { Animatable(if (enableAnimation) 0f else 1f) }

    // Start animations only if enabled
    if (enableAnimation) {
        LaunchedEffect(Unit) {
            // Scale animation
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, delayMillis = 100)
            )
            // Fade in animation
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, delayMillis = 300)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha.value)
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.size(24.dp))

            // App Name - Two lines, close together
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy((-12).dp)
            ) {
                Text(
                    text = "XIN CHAO,",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 0.8f
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "CAC BAN!",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = MaterialTheme.typography.displayLarge.lineHeight * 0.8f
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Splash Screen Preview")
@Composable
fun SplashContentPreview() {
    DATN_MobileTheme {
        SplashContent(
            onStartClick = {},
            enableAnimation = false // Disable animation for preview
        )
    }
}


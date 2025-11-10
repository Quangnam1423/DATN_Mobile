package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onStartClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Splash Screen")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartClick
            ) {
                Text(text = "Bắt Đầu")
            }
        }
    }
}

@Preview(showBackground = true, name = "màn hình chờ")
@Composable
fun SplashContentPreview() {
    DATN_MobileTheme {
        SplashContent(
            onStartClick = {}
        )
    }
}


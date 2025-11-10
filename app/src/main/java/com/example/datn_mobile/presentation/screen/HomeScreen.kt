package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Trang Chủ")
    }
}

@Preview(showBackground = true, name = "màn hình chủ")
@Composable
fun HomeScreenPreview() {
    DATN_MobileTheme {
        HomeScreen()
    }
}


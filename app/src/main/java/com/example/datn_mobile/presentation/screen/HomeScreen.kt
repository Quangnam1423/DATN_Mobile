package com.example.datn_mobile.presentation.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.DATN_MobileTheme

@Composable
fun HomeScreen (

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bạn đã đăng nhập thành công!", color = Color.Black)
    }
}

@Preview(showBackground = true, name = "default status")
@Composable
fun HomeScreenDefaultPreview() {
    DATN_MobileTheme {
        HomeScreen()
    }
}

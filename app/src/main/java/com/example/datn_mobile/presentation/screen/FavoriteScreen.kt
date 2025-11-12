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
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

@Composable
fun FavoriteScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Favorite Screen", color = Color.Black)
    }
}

@Preview(showBackground = true, name = "Favorite Screen")
@Composable
fun FavoriteScreenPreview() {
    DATN_MobileTheme {
        FavoriteScreen()
    }
}


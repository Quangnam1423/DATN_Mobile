package com.example.datn_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.datn_mobile.presentation.navigation.AppNavigation
import com.example.datn_mobile.ui.theme.DATN_MobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DATN_MobileTheme {
                AppNavigation()
            }
        }
    }
}

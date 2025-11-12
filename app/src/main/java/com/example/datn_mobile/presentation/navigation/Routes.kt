package com.example.datn_mobile.presentation.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash_screen")
    object Login : Routes("login_screen")
    object Register: Routes("register_screen")
    object Home : Routes("home_screen")
    object MyOrder : Routes("my_order_screen")
    object Favorite : Routes("favorite_screen")
    object MyProfile : Routes("my_profile_screen")
    object PrivacyPolicy : Routes("privacy_policy_screen")
    object Help : Routes("help_screen")
    object Detail : Routes("detail_screen/{itemId}") {
        fun createRoute(itemId: String) = "detail_screen/$itemId"
    }
}


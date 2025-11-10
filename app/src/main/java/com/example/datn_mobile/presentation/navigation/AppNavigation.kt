package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.HomeScreen
import com.example.datn_mobile.presentation.screen.LoginScreen
import com.example.datn_mobile.presentation.screen.SplashScreen

@Composable
fun AppNavigation () {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route // start point
    ) {
        composable(route = Routes.Splash.route) {
            SplashScreen(
                onTimeout = {
                    // Sau 2 giây tự động chuyển sang Login
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onStartClick = {
                    // Khi nhấn nút "Bắt Đầu", chuyển sang Home
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.Login.route)  {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        // delete Login Screen from backstack
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                }
            )
        }

        composable(route = Routes.Home.route) {
            HomeScreen()
        }

        composable(route = Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    //navController.popBackStack()
                    navController.navigate(Routes.Login.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

//        composable(route = Routes.Detail.route) { backStackEntry ->
//            val itemId = backStackEntry.arguments?.getString("itemId")
//            // DetailScreen(itemId = itemId)
//        }
    }
}
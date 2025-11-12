package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.FavoriteScreen
import com.example.datn_mobile.presentation.screen.HelpScreen
import com.example.datn_mobile.presentation.screen.HomeScreen
import com.example.datn_mobile.presentation.screen.LoginScreen
import com.example.datn_mobile.presentation.screen.MainScreen
import com.example.datn_mobile.presentation.screen.MyOrderScreen
import com.example.datn_mobile.presentation.screen.MyProfileScreen
import com.example.datn_mobile.presentation.screen.PrivacyPolicyScreen
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

        // Main screen with bottom navigation
        composable(route = Routes.Home.route) {
            MainScreen(navController = navController) {
                HomeScreen()
            }
        }
        
        composable(route = Routes.MyOrder.route) {
            MainScreen(navController = navController) {
                MyOrderScreen()
            }
        }
        
        composable(route = Routes.Favorite.route) {
            MainScreen(navController = navController) {
                FavoriteScreen()
            }
        }
        
        composable(route = Routes.MyProfile.route) {
            MainScreen(navController = navController) {
                MyProfileScreen(
                    navController = navController,
                    onLogoutSuccess = {
                        // Navigate to Login and clear backstack
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        
        composable(route = Routes.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(route = Routes.Help.route) {
            HelpScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
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
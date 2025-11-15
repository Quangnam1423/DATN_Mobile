package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.CartScreen
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
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
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
                    navController.popBackStack()
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
        
        composable(route = Routes.Cart.route) {
            MainScreen(navController = navController) {
                CartScreen()
            }
        }
        
        composable(route = Routes.MyProfile.route) {
            MainScreen(navController = navController) {
                MyProfileScreen(
                    navController = navController,
                    userProfile = null, // TODO: Get from ViewModel or State
                    onLogoutClick = {
                        // Navigate to Login and clear backstack
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Splash.route) {
                                inclusive = true
                            }
                        }
                    },
                    onEditProfileClick = {
                        // TODO: Navigate to Edit Profile screen
                        // navController.navigate(Routes.EditProfile.route)
                    },
                    onMyOrderClick = {
                        navController.navigate(Routes.MyOrder.route)
                    },
                    onCartClick = {
                        navController.navigate(Routes.Cart.route)
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
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
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
package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.EditProfileScreen
import com.example.datn_mobile.presentation.screen.ForgotPasswordScreen
import com.example.datn_mobile.presentation.screen.HomeScreenWithNav
import com.example.datn_mobile.presentation.screen.LoginScreen
import com.example.datn_mobile.presentation.screen.SearchScreen
import com.example.datn_mobile.presentation.screen.SplashScreen
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel
import com.example.datn_mobile.presentation.viewmodel.ProfileViewModel
import com.example.datn_mobile.presentation.viewmodel.SearchViewModel
import com.example.datn_mobile.presentation.viewmodel.SplashViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(route = Routes.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.Login.route) {
            LoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Routes.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val profileViewModel: ProfileViewModel = hiltViewModel()
            HomeScreenWithNav(
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                onProductClick = { _ ->
                    // TODO: Navigate to product detail screen
                    // navController.navigate(Routes.ProductDetail.route + "/$productId")
                },
                onAddToCartClick = { _ ->
                    // Check if user is logged in
                    // For now, navigate to login
                    navController.navigate(Routes.Login.route)
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.Search.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route)
                },
                onNavigateToEditProfile = {
                    navController.navigate(Routes.EditProfile.route)
                },
                onNavigateToCart = {
                    // TODO: Navigate to cart screen
                    navController.navigate(Routes.Cart.route)
                }
            )
        }

        composable(route = Routes.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onSearchSubmit = { _ ->
                    // TODO: Navigate to search results screen with query
                    // For now, just pop back
                    navController.popBackStack()
                },
                onRecentSearchClick = { _ ->
                    // TODO: Navigate to search results screen with keyword
                    // For now, just pop back
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

        composable(route = Routes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onResetClick = { email ->
                    // TODO: Call API to send reset email
                    // For now, just navigate back to login
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.ForgotPassword.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
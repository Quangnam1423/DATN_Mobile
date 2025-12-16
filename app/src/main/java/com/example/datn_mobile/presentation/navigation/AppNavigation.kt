package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.CartScreen
import com.example.datn_mobile.presentation.screen.EditProfileScreen
import com.example.datn_mobile.presentation.screen.ForgotPasswordScreen
import com.example.datn_mobile.presentation.screen.HelpScreen
import com.example.datn_mobile.presentation.screen.HomeScreenWithNav
import com.example.datn_mobile.presentation.screen.LoginScreen
import com.example.datn_mobile.presentation.screen.NotificationScreen
import com.example.datn_mobile.presentation.screen.PrivacyPolicyScreen
import com.example.datn_mobile.presentation.screen.ProductDetailScreen
import com.example.datn_mobile.presentation.screen.SearchScreen
import com.example.datn_mobile.presentation.screen.SplashScreen
import com.example.datn_mobile.presentation.screen.CheckoutScreen
import com.example.datn_mobile.presentation.screen.OrderTrackingScreen
import com.example.datn_mobile.presentation.screen.OrderDetailScreen
import com.example.datn_mobile.domain.model.Order
import com.example.datn_mobile.presentation.viewmodel.CartViewModel
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel
import com.example.datn_mobile.presentation.viewmodel.ProductDetailViewModel
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
            val cartViewModel: CartViewModel = hiltViewModel()
            HomeScreenWithNav(
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                onProductClick = { productId ->
                    navController.navigate(Routes.ProductDetail.route + "/$productId")
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
                },
                onNavigateToHelp = {
                    navController.navigate(Routes.Help.route)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Routes.PrivacyPolicy.route)
                },
                onNavigateToNotification = {
                    navController.navigate(Routes.Notification.route)
                },
                onNavigateToOrderTracking = {
                    navController.navigate(Routes.OrderTracking.route)
                },
                onSubmitRepairRequest = { phone, email, address, description ->
                    // Gọi placeOrder với type=1 (đơn sửa)
                    cartViewModel.placeOrder(
                        type = 1,
                        phoneNumber = phone,
                        email = email,
                        address = address,
                        description = description
                    )
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
                    // Không cần điều hướng, SearchScreen sẽ hiển thị kết quả ngay
                },
                onRecentSearchClick = { _ ->
                    // Không cần điều hướng, SearchScreen sẽ hiển thị kết quả ngay
                },
                onProductClick = { productId ->
                    navController.navigate(Routes.ProductDetail.route + "/$productId")
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
                },
                onNavigateToProfile = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.Cart.route) {
            val cartViewModel: CartViewModel = hiltViewModel()
            CartScreen(
                viewModel = cartViewModel,
                onBackClick = {
                    // Luôn quay về màn hình Home khi nhấn mũi tên back trên màn giỏ hàng
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onContinueShoppingClick = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onCheckoutClick = {
                    navController.navigate(Routes.Checkout.route)
                }
            )
        }

        composable(route = Routes.Checkout.route) {
            // Dùng chung CartViewModel với màn Cart để giữ lại trạng thái checkbox đã chọn
            val parentEntry = navController.getBackStackEntry(Routes.Cart.route)
            val cartViewModel: CartViewModel = hiltViewModel(parentEntry)
            CheckoutScreen(
                viewModel = cartViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onOrderSuccess = {
                    // Sau khi đặt hàng thành công quay về Home
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.ProductDetail.route + "/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val detailViewModel: ProductDetailViewModel = hiltViewModel()
            ProductDetailScreen(
                productId = productId,
                viewModel = detailViewModel,
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

        composable(route = Routes.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.Notification.route) {
            NotificationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.OrderTracking.route) {
            val orderTrackingViewModel: com.example.datn_mobile.presentation.viewmodel.OrderTrackingViewModel = hiltViewModel()
            OrderTrackingScreen(
                viewModel = orderTrackingViewModel,
                onBackClick = {
                    // Điều hướng về Home screen (tab Profile sẽ được hiển thị)
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onOrderClick = { order ->
                    orderTrackingViewModel.selectOrder(order)
                    navController.navigate(Routes.OrderDetail.createRoute(order.id))
                }
            )
        }

        composable(
            route = Routes.OrderDetail.route + "/{orderId}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            // Dùng chung ViewModel với OrderTracking để lấy order
            val parentEntry = navController.getBackStackEntry(Routes.OrderTracking.route)
            val orderTrackingViewModel: com.example.datn_mobile.presentation.viewmodel.OrderTrackingViewModel = hiltViewModel(parentEntry)
            val order = orderTrackingViewModel.getOrderById(orderId)
            
            if (order != null) {
                OrderDetailScreen(
                    order = order,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                // Nếu không tìm thấy order, quay lại
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}
package com.example.datn_mobile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datn_mobile.presentation.register.RegisterScreen
import com.example.datn_mobile.presentation.screen.HomeScreen
import com.example.datn_mobile.presentation.screen.LoginScreen

@Composable
fun AppNavigation () {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Register.route // start point
    ) {
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
            HomeScreen(

            )
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
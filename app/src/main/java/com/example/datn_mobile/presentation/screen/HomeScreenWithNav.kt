package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String
) {
    HOME("home", Icons.Outlined.Home, Icons.Filled.Home, "Trang chủ"),
    SEARCH("search", Icons.Outlined.Search, Icons.Filled.Search, "Tìm kiếm"),
    PROFILE("profile", Icons.Outlined.Person, Icons.Filled.Person, "Hồ sơ"),
    CART("cart", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart, "Giỏ hàng"),
}

@Composable
fun HomeScreenWithNav(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit,
    onAddToCartClick: (String) -> Unit
) {
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
            ) {
                // Home button
                IconButton(
                    onClick = {
                        selectedBottomItem = BottomNavItem.HOME
                        viewModel.loadProducts()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (selectedBottomItem == BottomNavItem.HOME)
                            BottomNavItem.HOME.selectedIcon else BottomNavItem.HOME.icon,
                        contentDescription = BottomNavItem.HOME.label
                    )
                }

                // Search button
                IconButton(
                    onClick = {
                        selectedBottomItem = BottomNavItem.SEARCH
                        onNavigateToSearch()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (selectedBottomItem == BottomNavItem.SEARCH)
                            BottomNavItem.SEARCH.selectedIcon else BottomNavItem.SEARCH.icon,
                        contentDescription = BottomNavItem.SEARCH.label
                    )
                }

                // Profile/User button
                IconButton(
                    onClick = {
                        selectedBottomItem = BottomNavItem.PROFILE
                        onNavigateToProfile()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (selectedBottomItem == BottomNavItem.PROFILE)
                            BottomNavItem.PROFILE.selectedIcon else BottomNavItem.PROFILE.icon,
                        contentDescription = BottomNavItem.PROFILE.label
                    )
                }

                // Cart button
                IconButton(
                    onClick = {
                        selectedBottomItem = BottomNavItem.CART
                        onNavigateToCart()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (selectedBottomItem == BottomNavItem.CART)
                            BottomNavItem.CART.selectedIcon else BottomNavItem.CART.icon,
                        contentDescription = BottomNavItem.CART.label
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show home content
            HomeScreenContent(
                viewModel = viewModel,
                onProductClick = onProductClick,
                onAddToCartClick = onAddToCartClick
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit
) {
    val products = viewModel.products.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Text(
            text = "Cửa hàng",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Loading state
        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
            return@Column
        }

        // Error state
        error.value?.let { errorMsg ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Lỗi: $errorMsg", color = Color.Red)
                    androidx.compose.material3.Button(onClick = { viewModel.loadProducts() }) {
                        Text("Thử lại")
                    }
                }
            }
            return@Column
        }

        // Empty state
        if (products.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "📭",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = "Không có sản phẩm nào",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Vui lòng quay lại sau",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    androidx.compose.material3.Button(onClick = { viewModel.loadProducts() }) {
                        Text("Tải lại")
                    }
                }
            }
            return@Column
        }

        // Products list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            items(products.value) { product ->
                ProductCard(
                    product = product,
                    onProductClick = onProductClick,
                    onAddToCartClick = onAddToCartClick
                )
            }
        }
    }
}


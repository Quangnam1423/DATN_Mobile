package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.datn_mobile.presentation.viewmodel.ProfileViewModel

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
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    onProductClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
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
                        homeViewModel.loadProducts()
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
            when (selectedBottomItem) {
                BottomNavItem.HOME -> {
                    HomeScreenContent(
                        viewModel = homeViewModel,
                        profileViewModel = profileViewModel,
                        onProductClick = onProductClick,
                        onAddToCartClick = onAddToCartClick,
                        onNavigateToProfile = {
                            selectedBottomItem = BottomNavItem.PROFILE
                        }
                    )
                }
                BottomNavItem.SEARCH -> {
                    onNavigateToSearch()
                }
                BottomNavItem.PROFILE -> {
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onLogoutClick = {
                            selectedBottomItem = BottomNavItem.HOME
                        },
                        onLoginClick = onNavigateToLogin,
                        onEditProfileClick = {
                            // Navigate to Edit Profile Screen
                            onNavigateToEditProfile()
                        }
                    )
                }
                BottomNavItem.CART -> {
                    onNavigateToCart()
                }
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val products = viewModel.products.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val profileState = profileViewModel.profileState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // User Header
        if (profileState.value.isAuthenticated && profileState.value.userProfile != null) {
            val profile = profileState.value.userProfile!!
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2196F3))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "👤",
                                fontSize = 24.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    // User Info
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = profile.fullName ?: "Người dùng",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = "ID: ${profile.id.take(8)}...",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 1
                        )
                    }
                }

                // Profile Button
                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

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
                CircularProgressIndicator()
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
                    Button(onClick = { viewModel.loadProducts() }) {
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
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Tải lại")
                    }
                }
            }
            return@Column
        }

        // Products list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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


package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.layout.layout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.example.datn_mobile.domain.model.Product
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.viewmodel.HomeViewModel
import com.example.datn_mobile.presentation.viewmodel.ProfileViewModel
import com.example.datn_mobile.utils.MessageManager
import java.util.Locale

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
    onAddToCartClick: (String) -> Unit,
    onNavigateToHelp: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onSubmitRepairRequest: (phone: String, email: String, description: String) -> Unit = { _, _, _ -> },
    onNavigateToProfileFromRepair: () -> Unit = {}
) {
    var selectedBottomItem by remember { mutableStateOf(BottomNavItem.HOME) }
    val profileState = profileViewModel.profileState.collectAsState()
    val isAuthenticated = profileState.value.isAuthenticated

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

                // Cart button with authentication check
                IconButton(
                    onClick = {
                        if (isAuthenticated) {
                            selectedBottomItem = BottomNavItem.CART
                            onNavigateToCart()
                        } else {
                            MessageManager.showError("Vui lòng đăng nhập để truy cập giỏ hàng")
                            onNavigateToLogin()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    BadgedBox(
                        badge = {
                            if (isAuthenticated) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text("!")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedBottomItem == BottomNavItem.CART)
                                BottomNavItem.CART.selectedIcon else BottomNavItem.CART.icon,
                            contentDescription = BottomNavItem.CART.label
                        )
                    }
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
                        },
                        onNavigateToLogin = onNavigateToLogin,  // ✅ Truyền callback
                        onNavigateToNotification = onNavigateToNotification,
                        onSubmitRepairRequest = onSubmitRepairRequest,
                        onNavigateToProfileFromRepair = {
                            selectedBottomItem = BottomNavItem.PROFILE
                            onNavigateToProfileFromRepair()
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
                        },
                        onNavigateToHelp = onNavigateToHelp,
                        onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy
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
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,  // ✅ Thêm callback
    onNavigateToNotification: () -> Unit,
    onSubmitRepairRequest: (phone: String, email: String, description: String) -> Unit,
    onNavigateToProfileFromRepair: () -> Unit
) {
    val homeState = viewModel.homeState.collectAsState()
    val state = homeState.value
    val profileState = profileViewModel.profileState.collectAsState()
    val isAuthenticated = profileState.value.isAuthenticated
    var selectedTab by remember { mutableStateOf(0) }

    // Show error message when error occurs
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // User Header
        if (isAuthenticated && profileState.value.userProfile != null) {
            val profile = profileState.value.userProfile!!
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PeachPinkAccent)
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

                    // User Info - chỉ hiển thị tên cuối cùng
                    val displayName = profile.fullName?.let { fullName ->
                        fullName.trim().split(" ").lastOrNull() ?: fullName
                    } ?: "Người dùng"
                    
                    Text(
                        text = "Xin chào, $displayName!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Notification Button
                BadgedBox(
                    badge = {
                        // TODO: Add badge count for unread notifications
                        // For now, we can add it later when we have notification count
                    }
                ) {
                    IconButton(
                        onClick = onNavigateToNotification,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Thông báo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Header
        Text(
            text = when (selectedTab) {
                2 -> "Sửa chữa điện thoại"
                else -> "Cửa hàng"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PeachPinkAccent,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = PeachPinkAccent
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Home", color = if (selectedTab == 0) PeachPinkAccent else Color.Gray) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Hãng sản phẩm", color = if (selectedTab == 1) PeachPinkAccent else Color.Gray) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Sửa chữa điện thoại", color = if (selectedTab == 2) PeachPinkAccent else Color.Gray) }
            )
        }

        // Loading state
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        // Empty state
        if (state.products.isEmpty()) {
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
                    Button(
                        onClick = { viewModel.loadProducts() },
                        colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent)
                    ) {
                        Text("Tải lại")
                    }
                }
            }
            return@Column
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> {
                // Home tab - Hiển thị tất cả sản phẩm
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.products) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
            1 -> {
                // Category tab - Hiển thị danh mục
                CategoryViewContent(
                    products = state.products,
                    onProductClick = onProductClick
                )
            }
            2 -> {
                RepairRequestTab(
                    onSubmit = onSubmitRepairRequest,
                    onNavigateToProfile = onNavigateToProfileFromRepair
                )
            }
        }
    }
}

@Composable
private fun RepairRequestTab(
    onSubmit: (phone: String, email: String, description: String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var device by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = device,
            onValueChange = { device = it },
            label = { Text("Dòng máy cần sửa") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = issue,
            onValueChange = { issue = it },
            label = { Text("Mô tả tình trạng") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                when {
                    phone.isBlank() -> {
                        MessageManager.showError("Vui lòng nhập số điện thoại")
                        return@Button
                    }
                    email.isBlank() -> {
                        MessageManager.showError("Vui lòng nhập email")
                        return@Button
                    }
                    device.isBlank() -> {
                        MessageManager.showError("Vui lòng nhập dòng máy cần sửa")
                        return@Button
                    }
                    issue.isBlank() -> {
                        MessageManager.showError("Vui lòng mô tả tình trạng")
                        return@Button
                    }
                }

                val description = "${device.trim()} | ${issue.trim()}"
                onSubmit(phone.trim(), email.trim(), description)
                MessageManager.showSuccess("Đã gửi yêu cầu sửa chữa")
                onNavigateToProfile()
            },
            colors = ButtonDefaults.buttonColors(containerColor = PeachPinkAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Gửi yêu cầu sửa chữa",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * CategoryViewContent - Hiển thị danh sách danh mục sản phẩm
 */
@Composable
fun CategoryViewContent(
    products: List<Product>,
    onProductClick: (String) -> Unit
) {
    // Extract categories from product names
    val categories = remember(products) {
        products.mapNotNull { product ->
            extractCategoryFromName(product.name)
        }.distinct().sorted()
    }
    
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    if (categories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "📂",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Không có hãng sản phẩm nào",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
        return
    }

    // Nếu đã chọn category, hiển thị sản phẩm của category đó
    if (selectedCategory != null) {
        val categoryProducts = products.filter { 
            extractCategoryFromName(it.name) == selectedCategory 
        }
        
        Column {
            // Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedCategory = null }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← Quay lại",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PeachPinkAccent
                )
            }
            
            // Category header
            Text(
                text = selectedCategory ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PeachPinkAccent,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Products list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categoryProducts) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick
                    )
                }
            }
        }
        return
    }

    // Hiển thị danh sách các category cards (chỉ hiển thị tên, không hiển thị sản phẩm)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryCardSimple(
                categoryName = category,
                productCount = products.count { extractCategoryFromName(it.name) == category },
                onClick = { selectedCategory = category }
            )
        }
    }
}

/**
 * CategoryCardSimple - Hiển thị một danh mục đơn giản (chỉ tên và số lượng sản phẩm)
 */
@Composable
fun CategoryCardSimple(
    categoryName: String,
    productCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = categoryName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PeachPinkAccent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$productCount sản phẩm",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "→",
                fontSize = 24.sp,
                color = Color(0xFF6200EA),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * CategoryProductItemContent - Hiển thị một sản phẩm trong danh mục
 */
@Composable
fun CategoryProductItemContent(
    product: Product,
    onProductClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product image
        AsyncImage(
            model = product.image,
            contentDescription = product.name,
            modifier = Modifier
                .size(80.dp)
                .weight(0.3f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Product info
        Column(
            modifier = Modifier.weight(0.7f)
        ) {
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            val displayPrice = ((product.variant.finalPrice
                ?: product.variant.price
                ?: 0.0)).toLong()
            Text(
                text = "${String.format(Locale.US, "%,d", displayPrice)} đ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}

/**
 * Extract brand/manufacturer from product name
 * Dựa vào tên sản phẩm để extract hãng sản phẩm
 */
private fun extractCategoryFromName(productName: String): String? {
    val name = productName.lowercase().trim()
    
    // Danh sách các hãng phổ biến và từ khóa nhận diện
    return when {
        // Apple / iPhone
        name.contains("iphone") || name.contains("apple") || name.startsWith("iphone") -> "iPhone"
        
        // Samsung
        name.contains("samsung") || name.startsWith("samsung") -> "Samsung"
        
        // Xiaomi
        name.contains("xiaomi") || name.contains("redmi") || name.contains("mi ") || name.startsWith("xiaomi") || name.startsWith("redmi") -> "Xiaomi"
        
        // Oppo
        name.contains("oppo") || name.startsWith("oppo") -> "Oppo"
        
        // Vivo
        name.contains("vivo") || name.startsWith("vivo") -> "Vivo"
        
        // Realme
        name.contains("realme") || name.contains("real me") || name.startsWith("realme") -> "Realme"
        
        // OnePlus
        name.contains("oneplus") || name.contains("one plus") || name.startsWith("oneplus") -> "OnePlus"
        
        // Huawei
        name.contains("huawei") || name.contains("honor") || name.startsWith("huawei") || name.startsWith("honor") -> "Huawei"
        
        // Nokia
        name.contains("nokia") || name.startsWith("nokia") -> "Nokia"
        
        // Motorola
        name.contains("motorola") || name.contains("moto ") || name.startsWith("motorola") || name.startsWith("moto") -> "Motorola"
        
        // Asus
        name.contains("asus") || name.contains("rog ") || name.startsWith("asus") -> "Asus"
        
        // Google Pixel
        name.contains("pixel") || name.contains("google") || name.startsWith("pixel") -> "Google Pixel"
        
        // Sony
        name.contains("sony") || name.contains("xperia") || name.startsWith("sony") -> "Sony"
        
        // LG
        name.contains("lg ") || name.startsWith("lg") -> "LG"
        
        // Dịch vụ và phụ kiện
        name.contains("dich vu") || name.contains("dịch vụ") -> "Dịch Vụ"
        name.contains("phu kien") || name.contains("phụ kiện") || name.contains("phụ kiện điện thoại") -> "Phụ Kiện"
        
        // Gaming phones
        name.contains("gaming") || name.contains("redmagic") || name.contains("black shark") -> "Điện Thoại Gaming"
        
        else -> null
    }
}

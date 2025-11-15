package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

// Data classes for demo
data class Banner(
    val id: String,
    val title: String,
    val imageUrl: String? = null
)

data class Category(
    val id: String,
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class Product(
    val id: String,
    val name: String,
    val price: Long,
    val originalPrice: Long? = null,
    val imageUrl: String? = null,
    val rating: Float = 0f
)

@Composable
fun HomeScreen() {
    // Demo data
    val banners = remember {
        listOf(
            Banner("1", "Khuyến mãi đặc biệt"),
            Banner("2", "Sản phẩm mới"),
            Banner("3", "Flash Sale")
        )
    }
    
    val categories = remember {
        listOf(
            Category("1", "iPhone", Icons.Filled.PhoneAndroid),
            Category("2", "Samsung", Icons.Filled.PhoneAndroid),
            Category("3", "Xiaomi", Icons.Filled.PhoneAndroid),
            Category("4", "Oppo", Icons.Filled.PhoneAndroid),
            Category("5", "Vivo", Icons.Filled.PhoneAndroid),
            Category("6", "Realme", Icons.Filled.PhoneAndroid)
        )
    }
    
    val featuredProducts = remember {
        listOf(
            Product("1", "iPhone 15 Pro Max", 29990000, 32990000, rating = 4.8f),
            Product("2", "Samsung Galaxy S24 Ultra", 27990000, 29990000, rating = 4.7f),
            Product("3", "Xiaomi 14 Pro", 19990000, 22990000, rating = 4.6f),
            Product("4", "Oppo Find X7", 18990000, 21990000, rating = 4.5f)
        )
    }
    
    val newArrivals = remember {
        listOf(
            Product("5", "iPhone 15", 24990000, rating = 4.9f),
            Product("6", "Samsung Galaxy S24", 19990000, rating = 4.8f),
            Product("7", "Xiaomi 14", 15990000, rating = 4.7f),
            Product("8", "Oppo Reno 11", 12990000, rating = 4.6f),
            Product("9", "Vivo X100", 17990000, rating = 4.8f),
            Product("10", "Realme GT 5 Pro", 14990000, rating = 4.5f)
        )
    }
    
    // Colors matching Login/Register/Splash screens
    val white = Color.White
    val darkGrey = Color(0xFF0C0C0C)
    val lightGrey = Color(0xFF867F7F)
    val lightBlue = Color(0xFF57A3DE)
    val lightGreyBackground = Color(0xFFF5F5F5)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(white),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
            // 1. Top App Bar with Search
            item {
                TopAppBarSection(
                    darkGrey = darkGrey,
                    lightGrey = lightGrey,
                    lightGreyBackground = lightGreyBackground
                )
            }
            
            // 2. Banner Carousel
            item {
                BannerCarouselSection(
                    banners = banners,
                    lightBlue = lightBlue
                )
            }
            
            // 3. Categories Grid
            item {
                CategoriesSection(
                    categories = categories,
                    lightBlue = lightBlue,
                    darkGrey = darkGrey
                )
            }
            
            // 4. Featured Products
            item {
                FeaturedProductsSection(
                    products = featuredProducts,
                    darkGrey = darkGrey,
                    lightGrey = lightGrey,
                    lightGreyBackground = lightGreyBackground
                )
            }
            
            // 5. New Arrivals
            item {
                NewArrivalsSection(
                    products = newArrivals,
                    darkGrey = darkGrey,
                    lightGrey = lightGrey,
                    lightGreyBackground = lightGreyBackground
                )
            }
        }
}

// 1. Top App Bar with Search Bar
@Composable
fun TopAppBarSection(
    darkGrey: Color,
    lightGrey: Color,
    lightGreyBackground: Color
) {
    var searchText by remember { mutableStateOf("") }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { 
                Text(
                    "Tìm kiếm điện thoại...",
                    color = lightGrey
                ) 
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = lightGrey
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = lightGreyBackground,
                focusedContainerColor = lightGreyBackground,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = lightGrey.copy(alpha = 0.5f)
            )
        )
        
        // Cart Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(lightGreyBackground)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Cart",
                tint = darkGrey,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Notification Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(lightGreyBackground)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = darkGrey,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// 2. Banner Carousel
@Composable
fun BannerCarouselSection(
    banners: List<Banner>,
    lightBlue: Color
) {
    var currentBannerIndex by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Banner Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(lightBlue.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = banners[currentBannerIndex].title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C0C0C)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            banners.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentBannerIndex) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentBannerIndex) 
                                lightBlue 
                            else 
                                lightBlue.copy(alpha = 0.3f)
                        )
                        .clickable { currentBannerIndex = index }
                )
                if (index < banners.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

// 3. Categories Grid
@Composable
fun CategoriesSection(
    categories: List<Category>,
    lightBlue: Color,
    darkGrey: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Các thương hiệu nổi tiếng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = darkGrey,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    lightBlue = lightBlue,
                    darkGrey = darkGrey
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    lightBlue: Color,
    darkGrey: Color
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(lightBlue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = lightBlue,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = darkGrey,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// 4. Featured Products
@Composable
fun FeaturedProductsSection(
    products: List<Product>,
    darkGrey: Color,
    lightGrey: Color,
    lightGreyBackground: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sản phẩm nổi bật",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = darkGrey
            )
            
            TextButton(onClick = { }) {
                Text(
                    text = "Xem tất cả",
                    fontSize = 14.sp,
                    color = lightGrey,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    darkGrey = darkGrey,
                    lightGrey = lightGrey,
                    lightGreyBackground = lightGreyBackground,
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}

// 5. New Arrivals
@Composable
fun NewArrivalsSection(
    products: List<Product>,
    darkGrey: Color,
    lightGrey: Color,
    lightGreyBackground: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sản phẩm mới",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = darkGrey
            )
            
            TextButton(onClick = { }) {
                Text(
                    text = "Xem tất cả",
                    fontSize = 14.sp,
                    color = lightGrey,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Grid layout - 2 columns
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            products.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            darkGrey = darkGrey,
                            lightGrey = lightGrey,
                            lightGreyBackground = lightGreyBackground,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty space if odd number of products
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Product Card Component
@Composable
fun ProductCard(
    product: Product,
    darkGrey: Color,
    lightGrey: Color,
    lightGreyBackground: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGreyBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Product Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(lightGrey.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhoneAndroid,
                    contentDescription = product.name,
                    tint = lightGrey,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            // Product Name
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = darkGrey,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            
            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${product.rating}",
                    fontSize = 12.sp,
                    color = lightGrey
                )
            }
            
            // Price
            Column {
                if (product.originalPrice != null) {
                    Text(
                        text = "${formatPrice(product.originalPrice)} đ",
                        fontSize = 12.sp,
                        color = lightGrey,
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }
                Text(
                    text = "${formatPrice(product.price)} đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkGrey
                )
            }
        }
    }
}

fun formatPrice(price: Long): String {
    return String.format("%,d", price).replace(",", ".")
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    DATN_MobileTheme {
        HomeScreen()
    }
}

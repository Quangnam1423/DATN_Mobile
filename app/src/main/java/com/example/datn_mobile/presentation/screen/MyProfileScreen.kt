package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.datn_mobile.presentation.navigation.Routes
import com.example.datn_mobile.presentation.viewmodel.LogoutViewModel
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

// Data class for profile options

data class ProfileOption(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit = {}
)

@Composable
fun MyProfileScreen(
    navController: NavController? = null,
    viewModel: LogoutViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit = {}
) {
    val logoutState by viewModel.logoutState.collectAsState()

    // Handle logout success - navigate to login
    LaunchedEffect(logoutState.isLogoutSuccess) {
        if (logoutState.isLogoutSuccess) {
            onLogoutSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Section
        TopProfileSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Middle Section - Action Buttons
        ActionButtonsSection()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Bottom Section - Profile Options
        ProfileOptionsSection(
            navController = navController,
            onLogoutClick = { viewModel.logout() }
        )
    }
}

@Composable
fun TopProfileSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Title
        Text(
            text = "My Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ScreenColors.darkGray,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        
        // Edit Icon
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit Profile",
            tint = ScreenColors.purple,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable { /* Handle edit */ }
        )
        
        // Profile Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                // Placeholder for profile image
                // You can replace this with actual image loading
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    tint = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Username
            Text(
                text = "USERNAME",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // User ID
            Text(
                text = "ID: XXXXXXXXX",
                fontSize = 14.sp,
                color = ScreenColors.darkGray
            )
        }
    }
}

@Composable
fun ActionButtonsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ScreenColors.purple),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Edit Button
        ActionButton(
            icon = Icons.Filled.Edit,
            label = "Edit",
            onClick = { /* Handle edit */ }
        )
        
        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(50.dp)
                .background(Color.White.copy(alpha = 0.5f))
        )
        
        // Wishlist Button
        ActionButton(
            icon = Icons.Filled.Favorite,
            label = "Favorites",
            onClick = { /* Handle wishlist */ }
        )
        
        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(50.dp)
                .background(Color.White.copy(alpha = 0.5f))
        )
        
        // My Orders Button
        ActionButton(
            icon = Icons.Filled.ShoppingBag,
            label = "My Orders",
            onClick = { /* Handle orders */ }
        )
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileOptionsSection(
    navController: NavController? = null,
    onLogoutClick: () -> Unit = {}
) {
    val options = listOf(
        ProfileOption("Privacy Policy", Icons.Filled.Key) {
            navController?.navigate(Routes.PrivacyPolicy.route)
        },
        ProfileOption("Payment Methods", Icons.Filled.CreditCard),
        ProfileOption("Notification", Icons.Filled.Notifications),
        ProfileOption("Settings", Icons.Filled.Settings),
        ProfileOption("Help", Icons.Filled.Handshake) {
            navController?.navigate(Routes.Help.route)
        },
        ProfileOption("Logout", Icons.Filled.Output) {
            onLogoutClick()
        }
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(options) { option ->
            ProfileOptionItem(option = option)
        }
    }
}

@Composable
fun ProfileOptionItem(option: ProfileOption) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = option.onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with light pink background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ScreenColors.lightPink.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = option.title,
                tint = ScreenColors.darkGray,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Option Title
        Text(
            text = option.title,
            fontSize = 16.sp,
            color = ScreenColors.darkGray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        
        // Arrow icon (for some options)
        if (option.title != "Logout") {
            Icon(
                imageVector = Icons.Filled.SubdirectoryArrowRight,
                contentDescription = null,
                tint = ScreenColors.darkGray.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "My Profile Screen")
@Composable
fun MyProfileScreenPreview() {
    DATN_MobileTheme {
        MyProfileScreen()
    }
}

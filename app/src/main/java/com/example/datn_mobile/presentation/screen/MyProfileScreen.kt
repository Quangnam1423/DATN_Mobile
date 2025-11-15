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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.presentation.navigation.Routes
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

// Screen colors matching app theme
//object ScreenColors {
//    val darkGray = Color(0xFF0C0C0C)
//    val lightGray = Color(0xFF867F7F)
//    val purple = Color(0xFF57A3DE) //
//    val lightPink = Color(0xFFFF6B9D)
//    val lightGrayBackground = Color(0xFFF5F5F5)
//    val errorRed = Color(0xFFE53935)
//    val lightBlue = Color(0xFFB4D3EA)
//}

// Data class for profile options
data class ProfileOption(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit = {}
)

@Composable
fun MyProfileScreen(
    navController: NavController? = null,
    userProfile: UserProfile? = null,
    onLogoutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onMyOrderClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header Section with Avatar
            item {
                ProfileHeaderSection(
                    userProfile = userProfile,
                    onEditClick = onEditProfileClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Quick Actions Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                QuickActionsSection(
                    onEditClick = onEditProfileClick,
                    onOrdersClick = onMyOrderClick,
                    onCartClick = onCartClick,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Personal Information Section
            item {
                PersonalInfoSection(
                    userProfile = userProfile,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Account Section
            item {
                ProfileSection(
                    title = "Tài khoản",
                    options = listOf(
                        ProfileOption(
                            title = "Thông báo",
                            icon = Icons.Filled.Notifications,
                            onClick = { /* Handle notifications */ }
                        ),
                        ProfileOption(
                            title = "Cài đặt",
                            icon = Icons.Filled.Settings,
                            onClick = { /* Handle settings */ }
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Support Section
            item {
                ProfileSection(
                    title = "Hỗ trợ",
                    options = listOf(
                        ProfileOption(
                            title = "Trợ giúp",
                            icon = Icons.Filled.Handshake,
                            onClick = { navController?.navigate(Routes.Help.route) }
                        ),
                        ProfileOption(
                            title = "Chính sách bảo mật",
                            icon = Icons.Filled.Key,
                            onClick = { navController?.navigate(Routes.PrivacyPolicy.route) }
                        )
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Logout Button
            item {
                LogoutButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

// Profile Header Section with background gradient
@Composable
fun ProfileHeaderSection(
    userProfile: UserProfile?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(ScreenColors.lightBlue)
    ) {
        // Background cloud shapes
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(120.dp)
                .offset(x = (-40).dp, y = (-30).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(100.dp)
                .offset(x = 30.dp, y = (-20).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.25f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(150.dp)
                .offset(x = (-30).dp, y = 80.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(110.dp)
                .offset(x = 40.dp, y = 60.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.25f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar title
            Text(
                text = "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(ScreenColors.purple.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(70.dp),
                        tint = ScreenColors.purple
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = userProfile?.fullName ?: userProfile?.email ?: "Khách hàng",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray
            )

        }
    }
}

// Personal Information Section
@Composable
fun PersonalInfoSection(
    userProfile: UserProfile?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Thông tin cá nhân",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ScreenColors.darkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = ScreenColors.lightGrayBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Phone Number
                userProfile?.phoneNumber?.let { phone ->
                    PersonalInfoItem(
                        icon = Icons.Filled.Phone,
                        label = "Số điện thoại",
                        value = phone
                    )
                    if (userProfile.email != null) {
                        Divider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }

                // Email
                userProfile?.email?.let { email ->
                    PersonalInfoItem(
                        icon = Icons.Filled.Email,
                        label = "Email",
                        value = email
                    )
                }
            }
        }
    }
}

@Composable
fun PersonalInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ScreenColors.purple.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = ScreenColors.purple,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = ScreenColors.lightGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                color = ScreenColors.darkGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Quick Actions Section
@Composable
fun QuickActionsSection(
    onEditClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ScreenColors.purple),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuickActionButton(
            icon = Icons.Filled.Edit,
            label = "Chỉnh sửa",
            onClick = onEditClick
        )

        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp),
            color = Color.White.copy(alpha = 0.3f)
        )

        QuickActionButton(
            icon = Icons.Filled.ShoppingBag,
            label = "Đơn hàng",
            onClick = onOrdersClick
        )

        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp),
            color = Color.White.copy(alpha = 0.3f)
        )

        QuickActionButton(
            icon = Icons.Filled.ShoppingCart,
            label = "Giỏ hàng",
            onClick = onCartClick
        )
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

// Profile Section
@Composable
fun ProfileSection(
    title: String,
    options: List<ProfileOption>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ScreenColors.darkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = ScreenColors.lightGrayBackground
            )
        ) {
            Column {
                options.forEachIndexed { index, option ->
                    ProfileOptionItem(
                        option = option,
                        showDivider = index < options.size - 1
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    option: ProfileOption,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = option.onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ScreenColors.purple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    tint = ScreenColors.purple,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = option.title,
                fontSize = 16.sp,
                color = ScreenColors.darkGray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Filled.SubdirectoryArrowRight,
                contentDescription = null,
                tint = ScreenColors.lightGray,
                modifier = Modifier.size(20.dp)
            )
        }

        if (showDivider) {
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}

// Logout Button
@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ScreenColors.errorRed
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Output,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Đăng xuất",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, name = "My Profile Screen")
@Composable
fun MyProfileScreenPreview() {
    DATN_MobileTheme {
        MyProfileScreen(
            userProfile = UserProfile(
                id = "user123",
                email = "example@email.com",
                fullName = "Nguyen Huy",
                phoneNumber = "0123456789",
                address = "Nguyen Trai, Thanh Xuan, Ha Noi",
                dob = "2003-01-10",
                roles = emptyList()
            )
        )
    }
}

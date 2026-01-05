package com.example.datn_mobile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.domain.model.Role
import com.example.datn_mobile.domain.model.UserProfile
import com.example.datn_mobile.presentation.viewmodel.ProfileViewModel
import com.example.datn_mobile.presentation.theme.LightGray
import com.example.datn_mobile.presentation.theme.LightPeachPink
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.compose.DATN_MobileTheme
import com.example.datn_mobile.utils.MessageManager

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNavigateToHelp: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToOrderTracking: () -> Unit = {}
) {
    val profileState = viewModel.profileState.collectAsState()

    if (!profileState.value.isAuthenticated) {
        // Not authenticated - show login prompt
        NotAuthenticatedScreen(onLoginClick = onLoginClick)
    } else {
        // Authenticated - show profile
        AuthenticatedProfileScreen(
            state = profileState.value,
            onRefresh = { viewModel.refreshProfile() },
            onLogout = {
                viewModel.logout()
                onLogoutClick()
            },
            onEditClick = onEditProfileClick,
            onNavigateToHelp = onNavigateToHelp,
            onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
            onNavigateToOrderTracking = onNavigateToOrderTracking
        )
    }
}

@Composable
private fun NotAuthenticatedScreen(onLoginClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            // Icon
            Text(
                text = "👤",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Title
            Text(
                text = "Vui lòng đăng nhập",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Description
            Text(
                text = "Để xem thông tin cá nhân và quản lý đơn hàng của bạn",
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Login button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PeachPinkAccent,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Đăng nhập ngay",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AuthenticatedProfileScreen(
    state: com.example.datn_mobile.presentation.viewmodel.ProfileState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onEditClick: () -> Unit,
    onNavigateToHelp: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToOrderTracking: () -> Unit = {}
) {
    // Show error message when error occurs
    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            MessageManager.showError(errorMsg)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Hồ sơ cá nhân",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PeachPinkAccent,
                modifier = Modifier.align(Alignment.Center)
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

        // Show profile even if some fields are null (user just registered)
        state.userProfile?.let { profile ->
            ProfileContent(
                profile = profile,
                onRefresh = onRefresh,
                onLogout = onLogout,
                onEditClick = onEditClick,
                hasError = state.error != null,
                onNavigateToHelp = onNavigateToHelp,
                onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                onNavigateToOrderTracking = onNavigateToOrderTracking
            )
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onEditClick: () -> Unit,
    hasError: Boolean,
    onNavigateToHelp: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToOrderTracking: () -> Unit = {}
) {
    // Check if profile is incomplete (all fields are null)
    val isProfileIncomplete = profile.fullName == null &&
        profile.address == null &&
        profile.dob == null &&
        profile.email == null &&
        profile.phoneNumber == null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Avatar section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = LightPeachPink,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "👤",
                        fontSize = 48.sp
                    )
                }
            }
        }

        // User name
        Text(
            text = profile.fullName ?: "Người dùng",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )

        // Incomplete Profile Warning
        if (isProfileIncomplete) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3CD),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ Hồ sơ chưa được cập nhật",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF856404)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vui lòng cập nhật thông tin cá nhân để có trải nghiệm tốt hơn",
                        fontSize = 12.sp,
                        color = Color(0xFF856404),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PeachPinkAccent,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Cập nhật ngay",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

//        // Role badge
//        Box(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .background(
//                    color = Color(0xFF2196F3).copy(alpha = 0.2f),
//                    shape = RoundedCornerShape(12.dp)
//                )
//                .padding(horizontal = 12.dp, vertical = 4.dp)
//        ) {
//            val roleDisplay = profile.roles.firstOrNull()?.displayName ?: "Người dùng"
//            Text(
//                text = roleDisplay,
//                fontSize = 12.sp,
//                color = Color(0xFF2196F3),
//                fontWeight = FontWeight.Medium
//            )
//        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile info card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightPeachPink
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Full Name
                ProfileInfoRow(
                    icon = Icons.Filled.Email,
                    label = "Họ tên",
                    value = profile.fullName ?: "Chưa cập nhật"
                )

                // Email
                ProfileInfoRow(
                    icon = Icons.Filled.Email,
                    label = "Email",
                    value = profile.email ?: "Chưa cập nhật"
                )

                // Phone
                ProfileInfoRow(
                    icon = Icons.Filled.Phone,
                    label = "Số điện thoại",
                    value = profile.phoneNumber ?: "Chưa cập nhật"
                )

                // Address
                ProfileInfoRow(
                    icon = Icons.Filled.LocationOn,
                    label = "Địa chỉ",
                    value = profile.address ?: "Chưa cập nhật"
                )

                // DOB
                ProfileInfoRow(
                    icon = Icons.Filled.Phone,
                    label = "Ngày sinh",
                    value = profile.dob ?: "Chưa cập nhật"
                )
            }
        }

        if (hasError) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Có lỗi khi tải dữ liệu",
                        fontSize = 12.sp,
                        color = Color(0xFFC62828),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { /* Refresh will be handled by parent */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh",
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Order Tracking button
        Button(
            onClick = onNavigateToOrderTracking,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PeachPinkAccent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Theo dõi đơn hàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Edit Profile button
        Button(
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PeachPinkAccent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Chỉnh Sửa Hồ Sơ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action buttons
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B6B),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Đăng xuất",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Refresh button
        OutlinedButton(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PeachPinkAccent
            )
        ) {
            Text(
                text = "Làm mới",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Help & Info section
        Text(
            text = "Hỗ trợ & Thông tin",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
        )

        // Help button
        OutlinedButton(
            onClick = onNavigateToHelp,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PeachPinkAccent
            )
        ) {
            Text(
                text = "Trợ giúp",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Privacy Policy button
        OutlinedButton(
            onClick = onNavigateToPrivacyPolicy,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PeachPinkAccent
            )
        ) {
            Text(
                text = "Chính sách bảo mật",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp),
                tint = PeachPinkAccent
            )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, name = "Not Authenticated Profile")
@Composable
fun NotAuthenticatedProfilePreview() {
    DATN_MobileTheme {
        NotAuthenticatedScreen(onLoginClick = {})
    }
}

@Preview(showBackground = true, name = "Authenticated Profile")
@Composable
fun AuthenticatedProfilePreview() {
    DATN_MobileTheme {
        AuthenticatedProfileScreen(
            state = com.example.datn_mobile.presentation.viewmodel.ProfileState(
                userProfile = UserProfile(
                    id = "user-001",
                    fullName = "Nguyễn Quang Nam",
                    address = "123 Đường Lê Lợi, TP.HCM",
                    dob = "2000-01-15",
                    email = "044@example.com",
                    phoneNumber = "0987654321",
                    roles = listOf(Role.USER)
                ),
                isAuthenticated = true,
                hasToken = true
            ),
            onRefresh = {},
            onLogout = {},
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Profile Loading")
@Composable
fun ProfileLoadingPreview() {
    DATN_MobileTheme {
        AuthenticatedProfileScreen(
            state = com.example.datn_mobile.presentation.viewmodel.ProfileState(
                isLoading = true,
                isAuthenticated = true,
                hasToken = true
            ),
            onRefresh = {},
            onLogout = {},
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Profile Error")
@Composable
fun ProfileErrorPreview() {
    DATN_MobileTheme {
        AuthenticatedProfileScreen(
            state = com.example.datn_mobile.presentation.viewmodel.ProfileState(
                error = "Lỗi kết nối: Không thể tải dữ liệu",
                isAuthenticated = true,
                hasToken = true
            ),
            onRefresh = {},
            onLogout = {},
            onEditClick = {}
        )
    }
}


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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.datn_mobile.data.network.dto.NotificationResponse
import com.example.datn_mobile.presentation.theme.PeachPinkAccent
import com.example.datn_mobile.presentation.viewmodel.NotificationViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val notificationState by viewModel.notificationState.collectAsState()
    val selectedNotificationState = remember { mutableStateOf<NotificationResponse?>(null) }
    val selectedTab = remember { mutableStateOf(0) } // 0 = Chưa đọc, 1 = Đã đọc

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ) {
        // Header
        NotificationHeader(onBackClick = onBackClick)

        // Tabs (custom row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val unreadSelected = selectedTab.value == 0
            val readSelected = selectedTab.value == 1

            Text(
                text = "Chưa đọc",
                fontSize = 16.sp,
                fontWeight = if (unreadSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (unreadSelected) PeachPinkAccent else Color.Gray,
                modifier = Modifier
                    .clickable { selectedTab.value = 0 }
                    .padding(vertical = 4.dp)
            )

            Text(
                text = "Đã đọc",
                fontSize = 16.sp,
                fontWeight = if (readSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (readSelected) PeachPinkAccent else Color.Gray,
                modifier = Modifier
                    .clickable { selectedTab.value = 1 }
                    .padding(vertical = 4.dp)
            )
        }

        // Content
        if (notificationState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (notificationState.notifications.isEmpty()) {
            EmptyNotificationScreen()
        } else {
            val all = notificationState.notifications
            val unreadList = all.filter { it.read == false }
            val readList = all.filter { it.read == true }

            val selected = selectedNotificationState.value
            if (selected != null) {
                // Khi đã chọn 1 thông báo → hiển thị full màn chi tiết
                NotificationDetail(
                    notification = selected,
                    onClose = { selectedNotificationState.value = null }
                )
            } else {
                // Danh sách theo tab
                when (selectedTab.value) {
                    0 -> {
                        NotificationList(
                            notifications = unreadList,
                            onNotificationClick = { notification ->
                                selectedNotificationState.value = notification
                                viewModel.markAsRead(notification)
                            }
                        )
                    }
                    1 -> {
                        NotificationList(
                            notifications = readList,
                            onNotificationClick = { notification ->
                                selectedNotificationState.value = notification
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PeachPinkAccent)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Quay lại",
                tint = Color.White
            )
        }

        Text(
            text = "Thông báo",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun EmptyNotificationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Không có thông báo nào",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bạn sẽ nhận được thông báo khi có cập nhật mới",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun NotificationList(
    notifications: List<NotificationResponse>,
    onNotificationClick: (NotificationResponse) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notifications) { notification ->
            NotificationCard(
                notification = notification,
                onClick = { onNotificationClick(notification) }
            )
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationResponse,
    onClick: () -> Unit
) {
    val isUnread = notification.read == false
    val backgroundColor = if (isUnread) {
        Color(0xFFECECEC)
    } else {
        Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnread) 2.dp else 1.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Unread indicator
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(10.dp) 
                        .background(PeachPinkAccent, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = notification.title ?: "Thông báo",
                    fontSize = 16.sp,
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body
                Text(
                    text = notification.body ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Time and Type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatNotificationTime(notification.createdAt),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    notification.type?.let { type ->
                        Text(
                            text = getNotificationTypeLabel(type),
                            fontSize = 12.sp,
                            color = PeachPinkAccent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationDetail(
    notification: NotificationResponse,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.title ?: "Chi tiết thông báo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Đóng",
                    fontSize = 14.sp,
                    color = PeachPinkAccent,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onClose() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.body ?: "",
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatNotificationTime(notification.createdAt),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

private fun formatNotificationTime(createdAt: String?): String {
    if (createdAt == null) return "Vừa xong"
    
    return try {
        val instant = Instant.parse(createdAt)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()
        val diff = java.time.Duration.between(dateTime, now)
        
        when {
            diff.toMinutes() < 1 -> "Vừa xong"
            diff.toMinutes() < 60 -> "${diff.toMinutes()} phút trước"
            diff.toHours() < 24 -> "${diff.toHours()} giờ trước"
            diff.toDays() < 7 -> "${diff.toDays()} ngày trước"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("vi"))
                dateTime.format(formatter)
            }
        }
    } catch (e: Exception) {
        "Vừa xong"
    }
}

private fun getNotificationTypeLabel(type: String): String {
    return when (type) {
        "ORDER_PLACED" -> "Đơn hàng"
        "ORDER_STATUS_UPDATE" -> "Cập nhật đơn hàng"
        "NEW_PROMOTION" -> "Khuyến mãi"
        "REPAIR_REQUEST_RECEIVED" -> "Sửa chữa"
        "REPAIR_STATUS_UPDATE" -> "Cập nhật sửa chữa"
        "REPAIR_TECHNICIAN_MESSAGE" -> "Tin nhắn"
        "GENERAL_ANNOUNCEMENT" -> "Thông báo"
        else -> type
    }
}


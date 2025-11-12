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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.ui.theme.DATN_MobileTheme

// Mock data for contact information
data class ContactInfo(
    val email: String,
    val phone: String,
    val address: String,
    val workingHours: String
)

val mockContactInfo = ContactInfo(
    email = "support@ptit.edu.vn",
    phone = "+84 123 456 789",
    address = "Học viện Công nghệ Bưu chính Viễn thông (PTIT)\nKm10, Đường Nguyễn Trãi, Hà Đông, Hà Nội",
    workingHours = "Thứ 2 - Thứ 6: 8:00 - 17:00\nThứ 7: 8:00 - 12:00\nChủ nhật: Nghỉ"
)

@Composable
fun HelpScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar with Back Button
        HelpScreenTopBar(
            title = "Trợ Giúp & Liên Hệ",
            onBackClick = onBackClick
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Title
            Text(
                text = "Trợ Giúp & Liên Hệ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Chúng tôi luôn sẵn sàng hỗ trợ bạn",
                fontSize = 14.sp,
                color = ScreenColors.darkGray.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Contact Information Section
            HelpSectionTitle("Thông Tin Liên Hệ")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Email
            ContactItem(
                icon = Icons.Filled.Email,
                title = "Email",
                content = mockContactInfo.email,
                onClick = { /* Handle email click */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Phone
            ContactItem(
                icon = Icons.Filled.Phone,
                title = "Điện Thoại",
                content = mockContactInfo.phone,
                onClick = { /* Handle phone click */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Address
            ContactItem(
                icon = Icons.Filled.LocationOn,
                title = "Địa Chỉ",
                content = mockContactInfo.address,
                onClick = { /* Handle address click */ }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Working Hours
            HelpSectionTitle("Giờ Làm Việc")
            HelpSectionContent(mockContactInfo.workingHours)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // FAQ Section
            HelpSectionTitle("Câu Hỏi Thường Gặp")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FAQItem(
                question = "Làm thế nào để đặt hàng?",
                answer = "Bạn có thể tìm kiếm sản phẩm trên trang chủ, chọn sản phẩm yêu thích và nhấn nút 'Mua ngay' hoặc 'Thêm vào giỏ hàng'."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FAQItem(
                question = "Phương thức thanh toán nào được chấp nhận?",
                answer = "Chúng tôi chấp nhận thanh toán qua thẻ tín dụng, thẻ ghi nợ, ví điện tử và thanh toán khi nhận hàng (COD)."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FAQItem(
                question = "Thời gian giao hàng là bao lâu?",
                answer = "Thời gian giao hàng thường từ 2-5 ngày làm việc tùy thuộc vào địa điểm giao hàng. Bạn sẽ nhận được thông báo cập nhật đơn hàng qua email hoặc SMS."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FAQItem(
                question = "Làm sao để theo dõi đơn hàng?",
                answer = "Bạn có thể theo dõi đơn hàng trong mục 'My Orders' trên ứng dụng. Nhập mã đơn hàng hoặc đăng nhập vào tài khoản để xem chi tiết."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FAQItem(
                question = "Có thể đổi trả sản phẩm không?",
                answer = "Có, bạn có thể đổi trả sản phẩm trong vòng 7 ngày kể từ ngày nhận hàng nếu sản phẩm còn nguyên vẹn và có hóa đơn mua hàng."
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with purple background
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    ScreenColors.purple.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = ScreenColors.purple,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ScreenColors.darkGray.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = ScreenColors.darkGray,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun FAQItem(
    question: String,
    answer: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                Color(0xFFF5F5F5),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = question,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ScreenColors.purple,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = answer,
            fontSize = 14.sp,
            color = ScreenColors.darkGray,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun HelpScreenTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ScreenColors.purple,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray
            )
        }
    }
}

@Composable
fun HelpSectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = ScreenColors.purple,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun HelpSectionContent(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = ScreenColors.darkGray,
        lineHeight = 22.sp
    )
}

@Preview(showBackground = true, name = "Help Screen")
@Composable
fun HelpScreenPreview() {
    DATN_MobileTheme {
        HelpScreen()
    }
}


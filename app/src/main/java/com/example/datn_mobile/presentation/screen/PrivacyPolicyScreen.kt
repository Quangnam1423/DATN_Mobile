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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar with Back Button
        TopBar(
            title = "Chính Sách Bảo Mật",
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
                text = "Chính Sách Bảo Mật",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ScreenColors.darkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Cập nhật lần cuối: Tháng 1 năm 2024",
                fontSize = 12.sp,
                color = ScreenColors.darkGray.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Section 1
            SectionTitle("1. Giới Thiệu")
            SectionContent(
                "Chào mừng bạn đến với Ứng dụng Mua bán điện thoại di động PTIT. Chúng tôi cam kết bảo vệ quyền riêng tư và đảm bảo an toàn thông tin cá nhân của bạn. Chính sách Bảo mật này giải thích cách chúng tôi thu thập, sử dụng, tiết lộ và bảo vệ thông tin của bạn khi bạn sử dụng ứng dụng di động của chúng tôi."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 2
            SectionTitle("2. Thông Tin Chúng Tôi Thu Thập")
            SectionContent(
                "Chúng tôi có thể thu thập thông tin mà bạn cung cấp trực tiếp cho chúng tôi, bao gồm:\n\n" +
                "• Thông tin nhận dạng cá nhân (tên, địa chỉ email, số điện thoại)\n" +
                "• Thông tin đăng nhập tài khoản\n" +
                "• Thông tin hồ sơ\n" +
                "• Thông tin thanh toán (được xử lý an toàn thông qua các nhà cung cấp bên thứ ba)\n" +
                "• Dữ liệu sử dụng và sở thích"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 3
            SectionTitle("3. Cách Chúng Tôi Sử Dụng Thông Tin Của Bạn")
            SectionContent(
                "Chúng tôi sử dụng thông tin thu thập được để:\n\n" +
                "• Cung cấp, duy trì và cải thiện dịch vụ của chúng tôi\n" +
                "• Xử lý giao dịch và gửi thông tin liên quan\n" +
                "• Gửi cho bạn thông báo kỹ thuật và tin nhắn hỗ trợ\n" +
                "• Phản hồi các bình luận và câu hỏi của bạn\n" +
                "• Theo dõi và phân tích xu hướng và cách sử dụng\n" +
                "• Cá nhân hóa trải nghiệm của bạn"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 4
            SectionTitle("4. Chia Sẻ Thông Tin")
            SectionContent(
                "Chúng tôi không bán, trao đổi hoặc cho thuê thông tin cá nhân của bạn cho bên thứ ba. Chúng tôi chỉ có thể chia sẻ thông tin của bạn trong các trường hợp sau:\n\n" +
                "• Với sự đồng ý của bạn\n" +
                "• Để tuân thủ nghĩa vụ pháp lý\n" +
                "• Để bảo vệ quyền và sự an toàn của chúng tôi\n" +
                "• Với các nhà cung cấp dịch vụ hỗ trợ chúng tôi vận hành ứng dụng"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 5
            SectionTitle("5. Bảo Mật Dữ Liệu")
            SectionContent(
                "Chúng tôi thực hiện các biện pháp bảo mật kỹ thuật và tổ chức phù hợp để bảo vệ thông tin cá nhân của bạn. Tuy nhiên, không có phương pháp truyền tải qua Internet hoặc lưu trữ điện tử nào là 100% an toàn."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 6
            SectionTitle("6. Quyền Của Bạn")
            SectionContent(
                "Bạn có quyền:\n\n" +
                "• Truy cập thông tin cá nhân của bạn\n" +
                "• Sửa dữ liệu không chính xác\n" +
                "• Yêu cầu xóa dữ liệu của bạn\n" +
                "• Phản đối việc xử lý dữ liệu của bạn\n" +
                "• Chuyển dữ liệu"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Section 7
            SectionTitle("7. Liên Hệ Với Chúng Tôi")
            SectionContent(
                "Nếu bạn có bất kỳ câu hỏi nào về Chính sách Bảo mật này, vui lòng liên hệ với chúng tôi tại:\n\n" +
                "Email: zxczxczxczxc@ptit.edu.vn\n" +
                "Địa chỉ: PTIT\n" +
                "Điện thoại: +84 123 456 789"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TopBar(
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
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = ScreenColors.purple,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SectionContent(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = ScreenColors.darkGray,
        lineHeight = 22.sp
    )
}

@Preview(showBackground = true, name = "Privacy Policy Screen")
@Composable
fun PrivacyPolicyScreenPreview() {
    DATN_MobileTheme {
        PrivacyPolicyScreen()
    }
}


package com.example.datn_mobile.presentation.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

/**
 * Màn hình WebView đơn giản để hiển thị trang thanh toán ZaloPay.
 * - Nhận vào orderUrl từ trước
 * - Có nút Back
 * - Hiển thị loading khi trang đang tải
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ZaloPayWebViewScreen(
    orderUrl: String,
    onBackClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Thanh toán ZaloPay") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        
                        // Thay đổi User-Agent để giả lập trình duyệt web thay vì mobile app
                        // Điều này khiến ZaloPay hiển thị trang web với QR code thay vì redirect về app
                        // User-Agent của Chrome trên desktop để ép hiển thị web version
                        val desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        settings.userAgentString = desktopUserAgent
                        
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                
                                // Tự động click vào option "Mở ứng dụng Zalopay" để hiển thị QR code
                                // Chỉ thực hiện nếu chưa ở trang QR code
                                if (url != null && !url.contains("/pay/v2/qr")) {
                                    view?.evaluateJavascript("""
                                        (function() {
                                            // Tìm và click vào option "Mở ứng dụng Zalopay" hoặc "Quét mã QR"
                                            var options = document.querySelectorAll('a, button, [role="button"], div[onclick]');
                                            for (var i = 0; i < options.length; i++) {
                                                var text = options[i].textContent || options[i].innerText || '';
                                                if (text.includes('Mở ứng dụng Zalopay') || 
                                                    text.includes('Quét mã QR') ||
                                                    text.includes('Zalopay') && text.includes('QR')) {
                                                    options[i].click();
                                                    return;
                                                }
                                            }
                                            
                                            // Nếu không tìm thấy, thử tìm link có href chứa 'qr'
                                            var links = document.querySelectorAll('a[href*="qr"], a[href*="QR"]');
                                            if (links.length > 0) {
                                                links[0].click();
                                                return;
                                            }
                                            
                                            // Hoặc thử tìm element có class/id chứa 'qr'
                                            var qrElements = document.querySelectorAll('[class*="qr"], [id*="qr"], [data-action*="qr"]');
                                            if (qrElements.length > 0) {
                                                qrElements[0].click();
                                            }
                                        })();
                                    """.trimIndent(), null)
                                }
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url?.toString() ?: return false

                                // 1) Khi ZaloPay redirect về endpoint return của backend, không hiển thị JSON,
                                //    mà đóng WebView để quay lại app
                                if (url.contains("/payment/zalopay/return")) {
                                    isLoading = false
                                    onBackClick()
                                    return true
                                }

                                // 2) Các URL http/https bình thường -> load trong WebView
                                //    Điều này cho phép hiển thị trang thanh toán với QR code
                                if (url.startsWith("http://") || url.startsWith("https://")) {
                                    return false
                                }

                                // 3) Scheme zalopay:// hoặc zalo:// -> thử mở app ZaloPay
                                //    Nếu không mở được, tự động chuyển đến trang QR code
                                if (url.startsWith("zalopay://") || url.startsWith("zalo://")) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                        // Mở được app -> chặn để không load URL trong WebView
                                        return true
                                    } catch (e: Exception) {
                                        // Không mở được app ZaloPay -> tự động load trang QR code
                                        // Parse URL hiện tại và thay đổi thành URL QR code
                                        view?.let { webView ->
                                            val currentUrl = webView.url
                                            if (currentUrl != null && currentUrl.contains("qcgateway.zalopay.vn")) {
                                                // Thử tìm URL QR code từ URL hiện tại
                                                val qrUrl = if (currentUrl.contains("/pay/v2/")) {
                                                    // Nếu đã có path /pay/v2/, thay đổi thành /pay/v2/qr
                                                    currentUrl.replace("/pay/v2/", "/pay/v2/qr")
                                                        .replace("/pay/v2\\?.*".toRegex(), "/pay/v2/qr?")
                                                } else {
                                                    // Nếu chưa có, thêm /pay/v2/qr
                                                    currentUrl.replace("qcgateway.zalopay.vn", "qcgateway.zalopay.vn/pay/v2/qr")
                                                }
                                                
                                                // Load URL QR code
                                                webView.post {
                                                    webView.loadUrl(qrUrl)
                                                }
                                            } else {
                                                // Nếu không parse được, thử inject JavaScript để click vào option QR
                                                webView.post {
                                                    webView.evaluateJavascript("""
                                                        (function() {
                                                            var options = document.querySelectorAll('a, button, [role="button"]');
                                                            for (var i = 0; i < options.length; i++) {
                                                                var text = (options[i].textContent || options[i].innerText || '').toLowerCase();
                                                                if (text.includes('mở ứng dụng') || text.includes('quét mã qr')) {
                                                                    var href = options[i].href || options[i].getAttribute('href') || '';
                                                                    if (href && !href.startsWith('zalo://') && !href.startsWith('zalopay://')) {
                                                                        window.location.href = href;
                                                                        return;
                                                                    }
                                                                }
                                                            }
                                                        })();
                                                    """.trimIndent(), null)
                                                }
                                            }
                                        }
                                        // Chặn để không load zalo:// URL
                                        return true
                                    }
                                }

                                // 4) Scheme market:// -> thử mở Play Store để cài app ZaloPay
                                //    Nếu không mở được, KHÔNG chặn để WebView tiếp tục hiển thị
                                if (url.startsWith("market://")) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        context.startActivity(intent)
                                        // Mở được Play Store -> chặn để không load URL trong WebView
                                        return true
                                    } catch (e: Exception) {
                                        // Không mở được Play Store -> KHÔNG chặn, để WebView tiếp tục hiển thị QR code
                                        return false
                                    }
                                }

                                // Các scheme khác -> để WebView tự xử lý
                                return false
                            }
                        }
                        loadUrl(Uri.decode(orderUrl))
                    }
                },
                update = { webView ->
                    webView.loadUrl(Uri.decode(orderUrl))
                }
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}



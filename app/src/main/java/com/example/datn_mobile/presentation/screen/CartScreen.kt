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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datn_mobile.ui.theme.DATN_MobileTheme
import java.text.NumberFormat
import java.util.Locale

// Screen colors matching app theme
object CartScreenColors {
    val darkGray = Color(0xFF0C0C0C)
    val lightGray = Color(0xFF867F7F)
    val lightBlue = Color(0xFF57A3DE) // Light blue matching theme
    val lightGrayBackground = Color(0xFFF5F5F5)
    val errorRed = Color(0xFFE53935)
}

// Helper function to format price as Int with thousand separators
fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    return "${formatter.format(price)} Đ"
}

// Data class for cart item
data class CartItem(
    val id: String,
    val imageUrl: String = "", // Placeholder - use actual image URLs
    val title: String,
    val description: String,
    val size: String,
    val price: Int,
    var quantity: Int = 1
)

@Composable
fun CartScreen() {
    // Sample cart items - replace with actual data from ViewModel/State
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(
                    id = "1",
                    title = "Product 1",
                    description = "Lorem ipsum dolor sit amet consectetur.",
                    size = "Pink, Size M",
                    price = 1000000,
                    quantity = 1
                ),
                CartItem(
                    id = "2",
                    title = "Product 2",
                    description = "Lorem ipsum dolor sit amet consectetur.",
                    size = "Pink, Size M",
                    price = 4500000,
                    quantity = 1
                )
            )
        )
    }

    val totalPrice = cartItems.sumOf { it.price * it.quantity }
    val itemCount = cartItems.sumOf { it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with title and badge
        CartHeader(
            itemCount = itemCount,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )

        // Cart items list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = cartItems,
                key = { it.id }
            ) { item ->
                CartItemCard(
                    item = item,
                    onQuantityChange = { itemId, newQuantity ->
                        if (newQuantity > 0) {
                            cartItems = cartItems.map {
                                if (it.id == itemId) it.copy(quantity = newQuantity)
                                else it
                            }
                        }
                    },
                    onDelete = { itemId ->
                        cartItems = cartItems.filter { it.id != itemId }
                    }
                )
            }
        }

        // Footer with total and checkout button
        CartFooter(
            totalPrice = totalPrice,
            onCheckoutClick = {
                // Handle checkout
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(CartScreenColors.lightGrayBackground)
                .padding(24.dp)
        )
    }
}

@Composable
fun CartHeader(
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Giỏ hàng",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CartScreenColors.darkGray
        )

        // Badge with item count
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(CartScreenColors.lightBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemCount.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (String, Int) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Product image with delete button
            Box(
                modifier = Modifier.size(100.dp)
            ) {
                // Product image placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CartScreenColors.lightGrayBackground)
                ) {
                    // Placeholder for image - replace with AsyncImage when you have image URLs
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Image",
                            fontSize = 12.sp,
                            color = CartScreenColors.lightGray
                        )
                    }
                }

                // Delete button overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-8).dp, y = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CartScreenColors.errorRed.copy(alpha = 0.8f))
                        .clickable { onDelete(item.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Product details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Product description
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = CartScreenColors.darkGray,
                    lineHeight = 20.sp
                )

                // Size
                Text(
                    text = item.size,
                    fontSize = 12.sp,
                    color = CartScreenColors.lightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price and quantity selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Text(
                        text = formatPrice(item.price),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CartScreenColors.darkGray
                    )

                    // Quantity selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minus button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CartScreenColors.lightBlue)
                                .clickable {
                                    if (item.quantity > 1) {
                                        onQuantityChange(item.id, item.quantity - 1)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Decrease",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Quantity display
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CartScreenColors.lightGrayBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.quantity.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = CartScreenColors.darkGray
                            )
                        }

                        // Plus button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CartScreenColors.lightBlue)
                                .clickable {
                                    onQuantityChange(item.id, item.quantity + 1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Increase",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartFooter(
    totalPrice: Int,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Total price
        Column {
            Text(
                text = "Tổng tiền",
                fontSize = 14.sp,
                color = CartScreenColors.lightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatPrice(totalPrice),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CartScreenColors.darkGray
            )
        }

        // Checkout button
        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .width(140.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CartScreenColors.lightBlue
            )
        ) {
            Text(
                text = "Thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, name = "Cart Screen")
@Composable
fun CartScreenPreview() {
    DATN_MobileTheme {
        CartScreen()
    }
}
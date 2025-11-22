# 🛒 CART API GUIDELINE - Hướng Dẫn Sử Dụng API Giỏ Hàng

## 📌 Tổng Quan

Hệ thống Cart & Order API cung cấp các chức năng quản lý giỏ hàng, xem giỏ hàng, đặt hàng và xem lịch sử đơn hàng.

**Base URL**: `http://localhost:8080/bej3`

**Authentication**: Yêu cầu JWT token trong header `Authorization: Bearer <token>`

---

## 🔄 Luồng Mua Hàng (Shopping Flow)

```
1. Đăng nhập → Lấy JWT token
   ↓
2. Duyệt sản phẩm → GET /home/product/{productId}
   ↓
3. Thêm vào giỏ hàng → POST /cart/add/{attId}
   ↓
4. Xem giỏ hàng → GET /cart/view
   ↓
5. Đặt hàng → POST /cart/place-order
   ↓
6. Kiểm tra đơn hàng → GET /cart/my-order
```

---

## 📚 API Endpoints Chi Tiết

### 1️⃣ **Thêm Sản Phẩm Vào Giỏ Hàng**

**Endpoint:** `POST /cart/add/{attId}`

**Mô Tả**: Thêm một sản phẩm (ProductAttribute) vào giỏ hàng của user hiện tại. Nếu sản phẩm đã có trong giỏ, số lượng sẽ được tăng lên 1.

**Path Parameters:**
| Parameter | Type | Mô Tả | Ví Dụ |
|-----------|------|-------|-------|
| `attId` | String | ID của ProductAttribute | `"550e8400-e29b-41d4-a716-446655440000"` |

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Response:** ✅ Success (201 Created)
```json
{
  "result": {
    "id": "abc123",
    "attId": "550e8400-e29b-41d4-a716-446655440000",
    "productName": "Áo T-shirt Premium",
    "productAttName": "Black Size M",
    "quantity": 1,
    "price": 250000,
    "color": "Đen",
    "img": "image-url.jpg"
  },
  "code": 1000,
  "message": "Success"
}
```

**Error Cases:**
| Case | Error Code | Message |
|------|-----------|---------|
| JWT token không hợp lệ | 1002 | UNAUTHENTICATED |
| ProductAttribute không tồn tại | 1001 | UNAUTHENTICATED |
| User không tồn tại | 1001 | USER_NOT_EXISTED |

**Ví Dụ Request (cURL):**
```bash
curl -X POST http://localhost:8080/bej3/cart/add/550e8400-e29b-41d4-a716-446655440000 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
const token = localStorage.getItem('jwtToken');
const attId = '550e8400-e29b-41d4-a716-446655440000';

fetch(`http://localhost:8080/bej3/cart/add/${attId}`, {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(data => console.log(data.result));
```

---

### 2️⃣ **Xem Giỏ Hàng (View Cart)**

**Endpoint:** `GET /cart/view`

**Mô Tả**: Lấy danh sách tất cả sản phẩm trong giỏ hàng của user hiện tại.

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Query Parameters:** None

**Response:** ✅ Success (200 OK)
```json
{
  "result": [
    {
      "id": "cart-item-1",
      "attId": "att-001",
      "productName": "Áo T-shirt Premium",
      "productAttName": "Black Size M",
      "quantity": 2,
      "price": 250000,
      "color": "Đen",
      "img": "https://example.com/image1.jpg"
    },
    {
      "id": "cart-item-2",
      "attId": "att-002",
      "productName": "Quần Jeans",
      "productAttName": "Blue Size 32",
      "quantity": 1,
      "price": 450000,
      "color": "Xanh",
      "img": "https://example.com/image2.jpg"
    }
  ],
  "code": 1000,
  "message": "Success"
}
```

**Tính Toán Tổng Tiền (Client-side):**
```javascript
const cartItems = response.result;
const totalPrice = cartItems.reduce((sum, item) => {
  return sum + (item.price * item.quantity);
}, 0);

console.log(`Tổng giỏ hàng: ${totalPrice.toLocaleString('vi-VN')} VNĐ`);
```

**Ví Dụ Request (cURL):**
```bash
curl -X GET http://localhost:8080/bej3/cart/view \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
const token = localStorage.getItem('jwtToken');

fetch('http://localhost:8080/bej3/cart/view', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(data => {
  console.log('Giỏ hàng:', data.result);
  const total = data.result.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  console.log('Tổng tiền:', total);
});
```

---

### 3️⃣ **Đặt Hàng (Place Order)**

**Endpoint:** `POST /cart/place-order`

**Mô Tả**: Tạo một đơn hàng từ các item trong giỏ hàng. Sau khi đặt hàng thành công, các item sẽ bị xóa khỏi giỏ.

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "phoneNumber": "0987654321",
  "email": "customer@example.com",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "description": "Giao vào buổi chiều, vui lòng gọi trước",
  "totalPrice": 450000,
  "items": [
    {
      "cartItemId": "cart-item-1",
      "productAttId": "att-001",
      "quantity": 1
    },
    {
      "cartItemId": "cart-item-2",
      "productAttId": "att-002",
      "quantity": 2
    }
  ]
}
```

**Request Body Fields:**

| Field | Type | Required | Mô Tả |
|-------|------|----------|-------|
| `phoneNumber` | String | ✅ | Số điện thoại giao hàng |
| `email` | String | ✅ | Email để nhận thông báo |
| `address` | String | ✅ | Địa chỉ giao hàng |
| `description` | String | ❌ | Ghi chú bổ sung cho đơn hàng |
| `totalPrice` | Double | ✅ | Tổng tiền (tính bằng client) |
| `items` | Array | ✅ | Danh sách sản phẩm trong đơn |
| `items[].cartItemId` | String | ✅ | ID của CartItem (từ /cart/view) |
| `items[].productAttId` | String | ✅ | ID của ProductAttribute |
| `items[].quantity` | Integer | ✅ | Số lượng sản phẩm |

**Response:** ✅ Success (200 OK)
```json
{
  "result": {
    "id": "order-001",
    "userName": "Nguyễn Văn A",
    "phoneNumber": "0987654321",
    "email": "customer@example.com",
    "address": "123 Đường ABC, Quận 1, TP.HCM",
    "description": "Giao vào buổi chiều, vui lòng gọi trước",
    "totalPrice": 950000,
    "orderAt": "2025-11-22",
    "updatedAt": "2025-11-22",
    "orderItems": [
      {
        "productName": "Áo T-shirt Premium",
        "productAttName": "Black Size M",
        "quantity": 1,
        "price": 250000,
        "color": "Đen",
        "img": "https://example.com/image1.jpg"
      },
      {
        "productName": "Quần Jeans",
        "productAttName": "Blue Size 32",
        "quantity": 2,
        "price": 450000,
        "color": "Xanh",
        "img": "https://example.com/image2.jpg"
      }
    ]
  },
  "code": 1000,
  "message": "Success"
}
```

**⚠️ Lưu Ý Quan Trọng:**
- ✅ Sau khi đặt hàng thành công, tất cả CartItems sẽ bị **xóa tự động**
- ✅ OrderID được sinh **tự động** bởi hệ thống
- ✅ OrderAt (ngày đặt hàng) được ghi nhận **tự động**
- ✅ TotalPrice nên tính trên **client-side** để chính xác

**Ví Dụ Tính Toán TotalPrice (Client-side):**
```javascript
const cartItems = [
  { price: 250000, quantity: 1 },
  { price: 450000, quantity: 2 }
];

const totalPrice = cartItems.reduce((sum, item) => 
  sum + (item.price * item.quantity), 0
);

console.log('Tổng cộng:', totalPrice); // 950000
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
const token = localStorage.getItem('jwtToken');
const cartItems = [
  {
    cartItemId: "cart-item-1",
    productAttId: "att-001",
    quantity: 1
  },
  {
    cartItemId: "cart-item-2",
    productAttId: "att-002",
    quantity: 2
  }
];

const totalPrice = 950000;

const orderRequest = {
  phoneNumber: "0987654321",
  email: "customer@example.com",
  address: "123 Đường ABC, Quận 1, TP.HCM",
  description: "Giao vào buổi chiều",
  totalPrice: totalPrice,
  items: cartItems
};

fetch('http://localhost:8080/bej3/cart/place-order', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(orderRequest)
})
.then(res => res.json())
.then(data => {
  if (data.code === 1000) {
    console.log('Đặt hàng thành công!');
    console.log('Order ID:', data.result.id);
    console.log('Tổng tiền:', data.result.totalPrice);
  } else {
    console.error('Lỗi:', data.message);
  }
});
```

---

### 4️⃣ **Xem Lịch Sử Đơn Hàng (My Orders)**

**Endpoint:** `GET /cart/my-order`

**Mô Tả**: Lấy danh sách tất cả đơn hàng của user hiện tại, sắp xếp từ mới nhất đến cũ nhất.

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Query Parameters:** None

**Response:** ✅ Success (200 OK)
```json
{
  "result": [
    {
      "id": "order-002",
      "userName": "Nguyễn Văn A",
      "phoneNumber": "0987654321",
      "email": "customer@example.com",
      "address": "123 Đường ABC, Quận 1, TP.HCM",
      "description": "Giao vào buổi chiều",
      "totalPrice": 950000,
      "orderAt": "2025-11-22",
      "updatedAt": "2025-11-22",
      "orderItems": [
        {
          "productName": "Áo T-shirt Premium",
          "productAttName": "Black Size M",
          "quantity": 1,
          "price": 250000,
          "color": "Đen",
          "img": "https://example.com/image1.jpg"
        }
      ]
    },
    {
      "id": "order-001",
      "userName": "Nguyễn Văn A",
      "phoneNumber": "0987654321",
      "email": "customer@example.com",
      "address": "456 Đường XYZ, Quận 3, TP.HCM",
      "description": "",
      "totalPrice": 500000,
      "orderAt": "2025-11-20",
      "updatedAt": "2025-11-20",
      "orderItems": [
        {
          "productName": "Quần Jeans",
          "productAttName": "Blue Size 32",
          "quantity": 1,
          "price": 500000,
          "color": "Xanh",
          "img": "https://example.com/image2.jpg"
        }
      ]
    }
  ],
  "code": 1000,
  "message": "Success"
}
```

**Ví Dụ Request (cURL):**
```bash
curl -X GET http://localhost:8080/bej3/cart/my-order \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
const token = localStorage.getItem('jwtToken');

fetch('http://localhost:8080/bej3/cart/my-order', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(data => {
  console.log('Lịch sử đơn hàng:', data.result);
  data.result.forEach(order => {
    console.log(`Đơn #${order.id} - ${order.totalPrice.toLocaleString('vi-VN')} VNĐ - ${order.orderAt}`);
  });
});
```

---

## 🔐 Authentication & Security

### Lấy JWT Token
```bash
POST /auth/log-in
{
  "phoneNumber": "0987654321",
  "password": "password123"
}
```

### Cách Sử Dụng Token
1. **Lưu token sau khi login:**
   ```javascript
   const token = response.result.token;
   localStorage.setItem('jwtToken', token);
   ```

2. **Gửi token trong mỗi request:**
   ```javascript
   const token = localStorage.getItem('jwtToken');
   headers: {
     'Authorization': `Bearer ${token}`,
     'Content-Type': 'application/json'
   }
   ```

3. **Xóa token khi logout:**
   ```javascript
   localStorage.removeItem('jwtToken');
   ```

---

## 📊 Data Models

### CartItem (Giỏ Hàng)
```java
{
  id: String,              // UUID của CartItem
  attId: String,           // Product Attribute ID
  productName: String,     // Tên sản phẩm
  productAttName: String,  // Tên biến thể (vd: "Black Size M")
  quantity: Integer,       // Số lượng
  price: Double,           // Giá của ProductAttribute
  color: String,           // Màu sắc
  img: String              // URL hình ảnh
}
```

### OrderRequest (Yêu Cầu Đặt Hàng)
```java
{
  phoneNumber: String,           // Số điện thoại giao hàng
  email: String,                 // Email liên hệ
  address: String,               // Địa chỉ giao hàng
  description: String,           // Ghi chú (optional)
  totalPrice: Double,            // Tổng tiền
  items: [
    {
      cartItemId: String,        // ID từ giỏ hàng
      productAttId: String,      // ID ProductAttribute
      quantity: Integer          // Số lượng
    }
  ]
}
```

### OrderDetailsResponse (Chi Tiết Đơn Hàng)
```java
{
  id: String,                    // Order ID (UUID)
  userName: String,              // Tên người nhận
  phoneNumber: String,           // Số điện thoại
  email: String,                 // Email
  address: String,               // Địa chỉ giao hàng
  description: String,           // Ghi chú
  totalPrice: Double,            // Tổng tiền đơn hàng
  orderAt: LocalDate,            // Ngày đặt hàng
  updatedAt: LocalDate,          // Ngày cập nhật gần nhất
  orderItems: [
    {
      productName: String,
      productAttName: String,
      quantity: Integer,
      price: Double,
      color: String,
      img: String
    }
  ]
}
```

---

## 🎯 Use Cases (Các Trường Hợp Sử Dụng)

### Use Case 1: Khách Hàng Mua Sản Phẩm Đơn Lẻ
```
1. POST /cart/add/{attId}                          ← Thêm 1 sản phẩm
2. GET /cart/view                                  ← Xem giỏ hàng
3. POST /cart/place-order (1 item)                 ← Đặt hàng luôn
4. GET /cart/my-order                              ← Xem đơn hàng vừa tạo
```

### Use Case 2: Khách Hàng Mua Nhiều Sản Phẩm
```
1. POST /cart/add/{attId1}                         ← Thêm sản phẩm 1
2. POST /cart/add/{attId2}                         ← Thêm sản phẩm 2
3. POST /cart/add/{attId3}                         ← Thêm sản phẩm 3
4. GET /cart/view                                  ← Xem toàn bộ giỏ hàng
5. POST /cart/place-order (3 items)                ← Đặt hàng
```

### Use Case 3: Khách Hàng Thay Đổi Số Lượng
```
1. POST /cart/add/{attId}                          ← Thêm sản phẩm (qty=1)
2. GET /cart/view                                  ← Xem giỏ
3. POST /cart/add/{attId}                          ← Thêm lại sản phẩm (qty=2)
4. GET /cart/view                                  ← Giỏ hiện có qty=2
5. POST /cart/place-order                          ← Đặt với qty=2
```

### Use Case 4: Xem Lịch Sử Mua Hàng
```
1. GET /cart/my-order                              ← Xem tất cả đơn hàng
2. Hiển thị danh sách đơn từ mới → cũ
```

---

## ⚠️ Các Lỗi Thường Gặp & Cách Xử Lý

### Error 1002: UNAUTHENTICATED
**Nguyên nhân:** JWT token không hợp lệ, hết hạn hoặc không được gửi
**Xử lý:**
```javascript
if (data.code === 1002) {
  console.error('Token hết hạn, vui lòng đăng nhập lại');
  localStorage.removeItem('jwtToken');
  window.location.href = '/login';
}
```

### Error 1001: USER_NOT_EXISTED
**Nguyên nhân:** User không tồn tại hoặc ProductAttribute không tìm thấy
**Xử lý:**
```javascript
if (data.code === 1001) {
  console.error('Sản phẩm không tồn tại hoặc người dùng không hợp lệ');
  // Redirect về trang sản phẩm
}
```

### Empty Cart
**Nguyên nhân:** Giỏ hàng trống (không có item)
**Xử lý:**
```javascript
if (data.result.length === 0) {
  console.log('Giỏ hàng trống');
  // Hiển thị thông báo "Chưa có sản phẩm"
}
```

---

## 💡 Best Practices

### 1. **Luôn Tính TotalPrice Trên Client-Side**
```javascript
const total = cartItems.reduce((sum, item) => 
  sum + (item.price * item.quantity), 0
);
```

### 2. **Lưu JWT Token Safely**
```javascript
// ✅ Tốt: Lưu trong localStorage (hoặc secure storage)
localStorage.setItem('jwtToken', token);

// ❌ Không nên: Lưu trong global variable
window.token = token;  // Dễ bị XSS attack
```

### 3. **Kiểm Tra Response Code Trước Khi Xử Dụng Data**
```javascript
.then(data => {
  if (data.code === 1000) {
    console.log('Thành công:', data.result);
  } else {
    console.error('Lỗi:', data.message);
  }
});
```

### 4. **Xử Lý Loading State**
```javascript
let isLoading = false;

async function addToCart(attId) {
  if (isLoading) return;
  isLoading = true;
  
  try {
    const response = await fetch(...);
    // Process response
  } finally {
    isLoading = false;
  }
}
```

### 5. **Hiển Thị Định Dạng Tiền VNĐ**
```javascript
function formatCurrency(amount) {
  return amount.toLocaleString('vi-VN', {
    style: 'currency',
    currency: 'VND'
  });
}

console.log(formatCurrency(250000)); // "250.000 ₫"
```

---

## 🧪 Cách Test API Bằng Postman

### 1. Import Environment
```json
{
  "id": "cart-api-env",
  "name": "Cart API Environment",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8080/bej3"
    },
    {
      "key": "token",
      "value": ""
    },
    {
      "key": "attId",
      "value": "550e8400-e29b-41d4-a716-446655440000"
    }
  ]
}
```

### 2. Test Sequence
```
1. POST /auth/log-in
   ├─ Save response.result.token as {{token}}
   
2. POST /cart/add/{{attId}}
   ├─ Header: Authorization: Bearer {{token}}
   
3. GET /cart/view
   ├─ Header: Authorization: Bearer {{token}}
   
4. POST /cart/place-order
   ├─ Header: Authorization: Bearer {{token}}
   ├─ Body: {...items...}
   
5. GET /cart/my-order
   ├─ Header: Authorization: Bearer {{token}}
```

---

## 📞 Support & Troubleshooting

| Problem | Solution |
|---------|----------|
| 404 Not Found | Kiểm tra base URL và endpoint path |
| 401 Unauthorized | JWT token hết hạn, đăng nhập lại |
| 400 Bad Request | Kiểm tra request body format |
| 500 Internal Server Error | Kiểm tra server logs |
| CORS Error | Đảm bảo frontend URL nằm trong CORS whitelist |

---

**Last Updated:** November 22, 2025  
**API Version:** 1.0.0  
**Status:** Production Ready

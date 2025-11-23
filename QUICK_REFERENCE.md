# 🚀 Quick Reference Guide - Home & Product Detail Implementation

## 📌 Changes Summary

This refactoring restructures the home page and product detail screens to strictly follow the **PRODUCT_API_GUIDELINE.md** specification.

---

## 🏠 HOME PAGE - What Changed?

### ❌ REMOVED (Old Implementation)
```kotlin
// Before: HomeScreen had these parameters
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit  // ❌ REMOVED
)

// Before: ProductCard had quantity selector
var quantity by remember { mutableStateOf(0) }
IconButton { quantity-- }  // Minus button ❌ REMOVED
IconButton { quantity++ }  // Plus button ❌ REMOVED
Button { onAddToCartClick(product.id) }  // ❌ REMOVED
```

### ✅ NEW (Current Implementation)
```kotlin
// After: Only product click parameter
fun HomeScreen(
    viewModel: HomeViewModel,
    onProductClick: (String) -> Unit  // ✅ Only this!
)

// After: ProductCard is simple
- Shows image ✅
- Shows name ✅
- Shows finalPrice ✅
- Shows originalPrice (if discount) ✅
- Shows createDate ✅
- "Xem Chi Tiết" button → navigates to ProductDetailScreen ✅
```

### 🎨 Home Page UI Now

```
┌─────────────────────────────────────────┐
│              Cửa hàng                   │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────────────────────────────┐  │
│  │                                  │  │
│  │  [Image of Product]              │  │
│  │                                  │  │
│  │  Áo T-shirt Premium              │  │
│  │                                  │  │
│  │  250.000 đ                       │  │
│  │  300.000 đ (strikethrough)       │  │
│  │                                  │  │
│  │  Ngày thêm: 2025-11-20           │  │
│  │                                  │  │
│  │     [Xem Chi Tiết]               │  │
│  │                                  │  │
│  └──────────────────────────────────┘  │
│                                         │
│  ┌──────────────────────────────────┐  │
│  │ (Next Product Card)              │  │
│  └──────────────────────────────────┘  │
│                                         │
└─────────────────────────────────────────┘
```

---

## 📦 PRODUCT DETAIL PAGE - New Feature

### ✅ NEW SCREEN: ProductDetailScreen

**When it's used:**
- User clicks "Xem Chi Tiết" on home page
- User clicks product card on home page

**What it shows:**
1. Main product image
2. Intro images (thumbnail list)
3. Product name
4. Variant selector (colors)
5. Detail images for selected variant
6. Attribute selector (sizes with prices)
7. Price info with discount
8. "Thêm vào giỏ hàng" button

### 🎨 Product Detail Page UI

```
┌──────────────────────────────────────────────┐
│  ← | Chi tiết sản phẩm                       │
├──────────────────────────────────────────────┤
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │                                        │ │
│  │      [Large Product Image]             │ │
│  │                                        │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  [T1] [T2] [T3]  (Intro image thumbnails)   │
│                                              │
│  Áo T-shirt Premium                          │
│                                              │
│  Chọn Màu Sắc                                │
│  [Đen] [Trắng] [Xanh]                        │
│                                              │
│  Ảnh chi tiết                                │
│  [D1] [D2] [D3]  (Detail image thumbnails)   │
│                                              │
│  Chọn Size                                   │
│  ┌──────────────────────────────────────┐   │
│  │ Size M                 250.000 đ      │   │
│  ├──────────────────────────────────────┤   │
│  │ Size L                 250.000 đ      │   │
│  ├──────────────────────────────────────┤   │
│  │ Size XL                250.000 đ      │   │
│  └──────────────────────────────────────┘   │
│                                              │
│  ───────────────────────────────────────    │
│  250.000 đ                                   │
│  300.000 đ (strikethrough)                   │
│  Giảm 17%                                    │
│  ───────────────────────────────────────    │
│                                              │
│  [🛒 Thêm vào giỏ hàng]                      │
│                                              │
└──────────────────────────────────────────────┘
```

### 🔄 Product Detail Features

**User Interactions:**
1. Select variant (color) → updates detail images
2. Select attribute (size) → shows that variant's price
3. Click "Thêm vào giỏ hàng" → calls `onAddToCartClick(attId)`
4. Click back arrow → returns to home page

**State Management:**
```kotlin
var selectedVariantIndex by remember { mutableStateOf(0) }
var selectedAttribute by remember { mutableStateOf<ProductDetailVariantAttribute?>(null) }
var mainImageUrl by remember { mutableStateOf(product.image) }

// Button is only enabled when attribute is selected
if (selectedAttribute != null) {
    Button { onAddToCartClick(selectedAttribute!!.id) }
} else {
    Button(enabled = false) { ... }
}
```

---

## 🔗 Navigation Flow

```
SplashScreen
    ↓
HomeScreen (GET /home)
    ↓
User clicks product
    ↓
ProductDetailScreen (GET /home/product/{productId})
    ↓
User selects variant + attribute + clicks "Thêm vào giỏ"
    ↓
POST /cart/add/{attId}
    ↓
Success/Error message
    ↓
Back to Home (or Cart)
```

---

## 📊 API Responses

### Home Page API
```kotlin
// GET /home
HomeResponse(
    result = listOf(
        Product(
            id = "product-001",
            name = "Áo T-shirt Premium",
            image = "https://...",
            status = 1,
            createDate = "2025-11-20",
            variant = ProductVariant(
                originalPrice = 300000,
                finalPrice = 250000,
                thumbnail = "https://..."
            )
        )
    ),
    code = 1000,
    message = "Success"
)
```

### Product Detail API
```kotlin
// GET /home/product/{productId}
ProductDetailResponse(
    result = ProductDetail(
        id = "product-001",
        name = "Áo T-shirt Premium",
        image = "https://...",
        status = 1,
        introImages = listOf(
            ProductDetailImage("https://intro-1.jpg"),
            ProductDetailImage("https://intro-2.jpg")
        ),
        variants = listOf(
            ProductDetailVariant(
                color = "Đen",
                detailImages = listOf(
                    ProductDetailImage("https://black-1.jpg")
                ),
                attributes = listOf(
                    ProductDetailVariantAttribute(
                        id = "attr-001",
                        name = "Size M",
                        originalPrice = 300000,
                        finalPrice = 250000,
                        discount = 16.67
                    )
                )
            )
        )
    ),
    code = 1000,
    message = "Success"
)
```

---

## 🗂️ File Locations

```
app/src/main/java/com/example/datn_mobile/

domain/
├── model/
│   └── ProductResponse.kt           [UPDATED]
└── repository/
    └── ProductRepository.kt         [UPDATED]

data/
├── network/api/
│   └── ProductApiService.kt         [UPDATED]
└── repository/
    └── ProductRepositoryImpl.kt      [UPDATED]

presentation/
├── navigation/
│   ├── AppNavigation.kt             [UPDATED]
│   └── Routes.kt                    [UPDATED]
├── viewmodel/
│   ├── HomeViewModel.kt             (unchanged)
│   └── ProductDetailViewModel.kt    [NEW]
└── screen/
    ├── HomeScreen.kt                [REFACTORED]
    ├── HomeScreenWithNav.kt         [UPDATED]
    └── ProductDetailScreen.kt       [NEW]
```

---

## ✅ Verification Checklist

After making these changes, verify:

- [ ] Home page loads and displays products
- [ ] Click on product card → navigates to product detail
- [ ] Product detail page displays all variants and attributes
- [ ] Selecting variant changes detail images
- [ ] Selecting attribute updates the price display
- [ ] "Thêm vào giỏ hàng" button is only enabled when attribute is selected
- [ ] Back button returns to home page
- [ ] Loading states display correctly
- [ ] Error states display appropriate messages
- [ ] API calls return proper response formats

---

## 🎯 Key Concepts

### Before (Incorrect)
- Home page tried to do everything: list, select, add to cart
- User confusion: where do I add quantity? Add to cart where?
- Complex state management on home page
- Not following API guideline

### After (Correct)
- **Home Page:** List products only (GET /home)
- **Detail Page:** Select options + add to cart (GET /home/product/{id}, POST /cart/add/{attId})
- **Clear Flow:** Browse → Click → Select → Add to Cart
- **Follows Guideline:** Exactly as specified in PRODUCT_API_GUIDELINE.md

---

## 🚀 Next Integration Steps

1. **Cart API**: Implement POST /cart/add/{attId} in backend
2. **Authentication**: Add JWT token check before add to cart
3. **Success Feedback**: Update MessageManager.showSuccess() calls
4. **Loading States**: Add proper loading indicators
5. **Error Handling**: Add retry logic for failed API calls

---

**Version:** 1.0.0  
**Last Updated:** 2025-11-22  
**Status:** ✅ Complete & Ready for Testing


# 🏠 PRODUCT API GUIDELINE - Hướng Dẫn Sử Dụng API Sản Phẩm & Hiển Thị Home

## 📌 Tổng Quan

Product API cung cấp các chức năng lấy danh sách sản phẩm để hiển thị trên trang chủ (Home) và chi tiết sản phẩm khi người dùng click vào một sản phẩm cụ thể.

**Base URL**: `http://localhost:8080/bej3`

**Authentication**: 
- ✅ `/home` - Không cần JWT token
- ✅ `/home/product/{productId}` - Không cần JWT token

---

## 🔄 Luồng Hiển Thị Home (Home Display Flow)

```
1. User vào trang Home
   ↓
2. Frontend gọi GET /home
   ├─ Lấy danh sách tất cả sản phẩm (status=1)
   ├─ Mỗi sản phẩm có:
   │  ├─ id, name, image
   │  ├─ variant (giá, giá gốc, ảnh thumbnail)
   │  └─ createDate
   └─ Hiển thị dạng grid/list

3. User click vào 1 sản phẩm
   ↓
4. Frontend gọi GET /home/product/{productId}
   ├─ Lấy chi tiết sản phẩm đầy đủ
   ├─ Gồm:
   │  ├─ Ảnh đại diện
   │  ├─ Ảnh chi tiết (introImages)
   │  └─ Tất cả variants với attributes
   └─ Hiển thị trang chi tiết sản phẩm

5. User chọn variant & attributes
   ↓
6. User click "Thêm vào giỏ"
   ↓
7. Frontend gọi POST /cart/add/{attId}
   (Xem CART_API_GUIDELINE.md)
```

---

## 📚 API Endpoints Chi Tiết

### 1️⃣ **Lấy Danh Sách Sản Phẩm (Home Page)**

**Endpoint:** `GET /home`

**Mô Tả**: Lấy danh sách tất cả sản phẩm có status=1 (đang bán) để hiển thị trên trang chủ. Sắp xếp từ mới nhất đến cũ nhất.

**Headers:**
```
Content-Type: application/json
```

**Query Parameters:** None

**Response:** ✅ Success (200 OK)
```json
{
  "result": [
    {
      "id": "product-001",
      "name": "Áo T-shirt Premium",
      "image": "https://example.com/main-image.jpg",
      "status": 1,
      "createDate": "2025-11-20",
      "variant": {
        "originalPrice": 300000,
        "finalPrice": 250000,
        "thumbnail": "https://example.com/variant-thumb.jpg"
      }
    },
    {
      "id": "product-002",
      "name": "Quần Jeans Classic",
      "image": "https://example.com/jeans-main.jpg",
      "status": 1,
      "createDate": "2025-11-18",
      "variant": {
        "originalPrice": 600000,
        "finalPrice": 480000,
        "thumbnail": "https://example.com/jeans-thumb.jpg"
      }
    },
    {
      "id": "product-003",
      "name": "Áo Sơ Mi Formal",
      "image": "https://example.com/shirt-main.jpg",
      "status": 1,
      "createDate": "2025-11-15",
      "variant": {
        "originalPrice": 450000,
        "finalPrice": 360000,
        "thumbnail": "https://example.com/shirt-thumb.jpg"
      }
    }
  ],
  "code": 1000,
  "message": "Success"
}
```

**Response Fields:**
| Field | Type | Mô Tả |
|-------|------|-------|
| `id` | String | UUID của sản phẩm |
| `name` | String | Tên sản phẩm |
| `image` | String | URL ảnh đại diện sản phẩm |
| `status` | Integer | Trạng thái (1=đang bán, 0=ngừng kinh doanh) |
| `createDate` | Date | Ngày tạo sản phẩm |
| `variant.originalPrice` | Double | Giá gốc |
| `variant.finalPrice` | Double | Giá bán (sau giảm giá) |
| `variant.thumbnail` | String | URL ảnh thumbnail của variant |

**Ví Dụ Request (cURL):**
```bash
curl -X GET http://localhost:8080/bej3/home \
  -H "Content-Type: application/json"
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
fetch('http://localhost:8080/bej3/home')
  .then(res => res.json())
  .then(data => {
    if (data.code === 1000) {
      const products = data.result;
      console.log(`Tổng ${products.length} sản phẩm`);
      
      products.forEach(product => {
        console.log(`
          ${product.name}
          Giá: ${product.variant.finalPrice.toLocaleString('vi-VN')} VNĐ
          (Giá gốc: ${product.variant.originalPrice.toLocaleString('vi-VN')} VNĐ)
        `);
      });
    }
  });
```

**Hiển Thị Trên Home Page (React/Vue Example):**
```javascript
// React Example
import { useState, useEffect } from 'react';

export function HomePage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/bej3/home')
      .then(res => res.json())
      .then(data => {
        if (data.code === 1000) {
          setProducts(data.result);
        }
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Đang tải sản phẩm...</div>;

  return (
    <div className="products-grid">
      {products.map(product => (
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  );
}

function ProductCard({ product }) {
  const discount = Math.round(
    ((product.variant.originalPrice - product.variant.finalPrice) / 
     product.variant.originalPrice) * 100
  );

  return (
    <div className="product-card">
      <img src={product.image} alt={product.name} />
      
      {discount > 0 && (
        <div className="discount-badge">-{discount}%</div>
      )}
      
      <h3>{product.name}</h3>
      
      <div className="price">
        <span className="final-price">
          {product.variant.finalPrice.toLocaleString('vi-VN')} VNĐ
        </span>
        {discount > 0 && (
          <span className="original-price">
            {product.variant.originalPrice.toLocaleString('vi-VN')} VNĐ
          </span>
        )}
      </div>
      
      <a href={`/product/${product.id}`} className="view-button">
        Xem Chi Tiết
      </a>
    </div>
  );
}
```

---

### 2️⃣ **Lấy Chi Tiết Sản Phẩm (Product Details)**

**Endpoint:** `GET /home/product/{productId}`

**Mô Tả**: Lấy chi tiết đầy đủ của một sản phẩm, bao gồm tất cả variants, attributes, và hình ảnh chi tiết. Dùng khi user click vào sản phẩm trên home.

**Path Parameters:**
| Parameter | Type | Mô Tả | Ví Dụ |
|-----------|------|-------|-------|
| `productId` | String | ID của sản phẩm | `"product-001"` |

**Headers:**
```
Content-Type: application/json
```

**Response:** ✅ Success (200 OK)
```json
{
  "result": {
    "id": "product-001",
    "name": "Áo T-shirt Premium",
    "image": "https://example.com/main-image.jpg",
    "status": 1,
    "introImages": [
      {
        "url": "https://example.com/intro-1.jpg"
      },
      {
        "url": "https://example.com/intro-2.jpg"
      },
      {
        "url": "https://example.com/intro-3.jpg"
      }
    ],
    "variants": [
      {
        "color": "Đen",
        "detailImages": [
          {
            "url": "https://example.com/black-detail-1.jpg"
          },
          {
            "url": "https://example.com/black-detail-2.jpg"
          }
        ],
        "attributes": [
          {
            "id": "attr-001",
            "name": "Black Size M",
            "originalPrice": 300000,
            "finalPrice": 250000,
            "discount": 16.67
          },
          {
            "id": "attr-002",
            "name": "Black Size L",
            "originalPrice": 300000,
            "finalPrice": 250000,
            "discount": 16.67
          }
        ]
      },
      {
        "color": "Trắng",
        "detailImages": [
          {
            "url": "https://example.com/white-detail-1.jpg"
          },
          {
            "url": "https://example.com/white-detail-2.jpg"
          }
        ],
        "attributes": [
          {
            "id": "attr-003",
            "name": "White Size M",
            "originalPrice": 300000,
            "finalPrice": 250000,
            "discount": 16.67
          },
          {
            "id": "attr-004",
            "name": "White Size L",
            "originalPrice": 300000,
            "finalPrice": 250000,
            "discount": 16.67
          }
        ]
      }
    ]
  },
  "code": 1000,
  "message": "Success"
}
```

**Response Fields Explanation:**

| Field | Type | Mô Tả |
|-------|------|-------|
| `id` | String | UUID của sản phẩm |
| `name` | String | Tên sản phẩm |
| `image` | String | URL ảnh đại diện |
| `status` | Integer | Trạng thái sản phẩm |
| `introImages[]` | Array | Danh sách ảnh giới thiệu sản phẩm |
| `introImages[].url` | String | URL ảnh giới thiệu |
| `variants[]` | Array | Danh sách phiên bản sản phẩm (theo màu sắc, size, v.v) |
| `variants[].color` | String | Tên màu/phiên bản |
| `variants[].detailImages[]` | Array | Ảnh chi tiết của variant này |
| `variants[].attributes[]` | Array | Danh sách loại/size của variant này |
| `variants[].attributes[].id` | String | **ID ProductAttribute** (dùng để add to cart) |
| `variants[].attributes[].name` | String | Tên attribute (vd: "Size M", "Size L") |
| `variants[].attributes[].originalPrice` | Double | Giá gốc |
| `variants[].attributes[].finalPrice` | Double | Giá bán (sau giảm) |
| `variants[].attributes[].discount` | Double | % giảm giá |

**Ví Dụ Request (cURL):**
```bash
curl -X GET "http://localhost:8080/bej3/home/product/product-001" \
  -H "Content-Type: application/json"
```

**Ví Dụ Request (JavaScript/Fetch):**
```javascript
const productId = 'product-001';

fetch(`http://localhost:8080/bej3/home/product/${productId}`)
  .then(res => res.json())
  .then(data => {
    if (data.code === 1000) {
      const product = data.result;
      console.log(`Chi tiết: ${product.name}`);
      console.log(`Tổng ${product.variants.length} phiên bản`);
      
      product.variants.forEach(variant => {
        console.log(`\nVariant: ${variant.color}`);
        console.log(`  - ${variant.attributes.length} loại size/kiểu`);
        variant.attributes.forEach(attr => {
          console.log(`    * ${attr.name}: ${attr.finalPrice.toLocaleString('vi-VN')} VNĐ`);
        });
      });
    }
  });
```

**Hiển Thị Chi Tiết Sản Phẩm (React Example):**
```javascript
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

export function ProductDetailPage() {
  const { productId } = useParams();
  const [product, setProduct] = useState(null);
  const [selectedVariant, setSelectedVariant] = useState(0);
  const [selectedAttribute, setSelectedAttribute] = useState(null);
  const [mainImage, setMainImage] = useState('');

  useEffect(() => {
    fetch(`http://localhost:8080/bej3/home/product/${productId}`)
      .then(res => res.json())
      .then(data => {
        if (data.code === 1000) {
          setProduct(data.result);
          setMainImage(data.result.image);
        }
      });
  }, [productId]);

  if (!product) return <div>Đang tải...</div>;

  const currentVariant = product.variants[selectedVariant];

  const handleAddToCart = async () => {
    if (!selectedAttribute) {
      alert('Vui lòng chọn size/kiểu');
      return;
    }

    const token = localStorage.getItem('jwtToken');
    if (!token) {
      alert('Vui lòng đăng nhập trước');
      window.location.href = '/login';
      return;
    }

    const response = await fetch(
      `http://localhost:8080/bej3/cart/add/${selectedAttribute.id}`,
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    const data = await response.json();
    if (data.code === 1000) {
      alert('Thêm vào giỏ hàng thành công!');
    } else {
      alert('Lỗi: ' + data.message);
    }
  };

  return (
    <div className="product-detail-page">
      {/* Hình ảnh */}
      <div className="product-images">
        <div className="main-image">
          <img src={mainImage} alt={product.name} />
        </div>
        <div className="thumbnail-list">
          {product.introImages.map((img, idx) => (
            <img
              key={idx}
              src={img.url}
              alt="intro"
              onClick={() => setMainImage(img.url)}
              className="thumbnail"
            />
          ))}
        </div>
      </div>

      {/* Thông tin sản phẩm */}
      <div className="product-info">
        <h1>{product.name}</h1>

        {/* Chọn Variant (Màu sắc) */}
        <div className="variant-selector">
          <label>Chọn Màu Sắc:</label>
          <div className="variant-buttons">
            {product.variants.map((variant, idx) => (
              <button
                key={idx}
                className={`variant-btn ${selectedVariant === idx ? 'active' : ''}`}
                onClick={() => {
                  setSelectedVariant(idx);
                  setSelectedAttribute(null);
                  setMainImage(product.image);
                }}
              >
                {variant.color}
              </button>
            ))}
          </div>

          {/* Ảnh chi tiết của variant đã chọn */}
          {currentVariant && currentVariant.detailImages.length > 0 && (
            <div className="variant-detail-images">
              {currentVariant.detailImages.map((img, idx) => (
                <img
                  key={idx}
                  src={img.url}
                  alt="detail"
                  onClick={() => setMainImage(img.url)}
                  className="detail-thumb"
                />
              ))}
            </div>
          )}
        </div>

        {/* Chọn Size/Kiểu */}
        <div className="attribute-selector">
          <label>Chọn Size:</label>
          <div className="attribute-buttons">
            {currentVariant && currentVariant.attributes.map(attr => (
              <button
                key={attr.id}
                className={`attribute-btn ${selectedAttribute?.id === attr.id ? 'active' : ''}`}
                onClick={() => setSelectedAttribute(attr)}
              >
                <div>{attr.name}</div>
                <div className="price">
                  {attr.finalPrice.toLocaleString('vi-VN')} VNĐ
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Giá */}
        {selectedAttribute && (
          <div className="price-section">
            <div className="discount-percent">
              -{Math.round(selectedAttribute.discount)}%
            </div>
            <div className="final-price">
              {selectedAttribute.finalPrice.toLocaleString('vi-VN')} VNĐ
            </div>
            <div className="original-price">
              {selectedAttribute.originalPrice.toLocaleString('vi-VN')} VNĐ
            </div>
          </div>
        )}

        {/* Nút Add to Cart */}
        <button
          className="add-to-cart-btn"
          onClick={handleAddToCart}
          disabled={!selectedAttribute}
        >
          🛒 Thêm Vào Giỏ Hàng
        </button>
      </div>
    </div>
  );
}
```

---

## 📊 Data Structure Visualization

### Home Page - Hiển Thị Dạng Grid

```
┌─────────────────────────────────────────────────────┐
│          GET /home - Danh Sách Sản Phẩm             │
└─────────────────────────────────────────────────────┘

┌────────────┐  ┌────────────┐  ┌────────────┐
│  Sản phẩm1 │  │  Sản phẩm2 │  │  Sản phẩm3 │
├────────────┤  ├────────────┤  ├────────────┤
│ [Image]    │  │ [Image]    │  │ [Image]    │
│            │  │            │  │            │
│ Tên: ...   │  │ Tên: ...   │  │ Tên: ...   │
│ Giá: 250k  │  │ Giá: 480k  │  │ Giá: 360k  │
│ [Xem]      │  │ [Xem]      │  │ [Xem]      │
└────────────┘  └────────────┘  └────────────┘
```

### Product Detail Page - Chi Tiết Sản Phẩm

```
┌──────────────────────────────────────────────────────────────┐
│      GET /home/product/{productId} - Chi Tiết Sản Phẩm      │
└──────────────────────────────────────────────────────────────┘

┌────────────────────────┐  ┌──────────────────────────────┐
│   Main Image           │  │   Thông Tin Sản Phẩm        │
│   [Large Photo]        │  │                              │
├────────────────────────┤  │   Tên Sản Phẩm               │
│ [T1] [T2] [T3]         │  │                              │
└────────────────────────┘  │   Chọn Màu:                  │
                            │   [Đen] [Trắng] [Xanh]       │
                            │                              │
                            │   Chọn Size:                 │
                            │   [M: 250k] [L: 250k]        │
                            │                              │
                            │   Giá: 250.000 VNĐ           │
                            │   (Giá gốc: 300.000 VNĐ)    │
                            │                              │
                            │   [🛒 Thêm Vào Giỏ]         │
                            └──────────────────────────────┘
```

### Product Hierarchy

```
Product
├─ id: "product-001"
├─ name: "Áo T-shirt Premium"
├─ image: "main-image.jpg"
├─ status: 1
├─ introImages: [
│   ├─ url: "intro-1.jpg"
│   ├─ url: "intro-2.jpg"
│   └─ url: "intro-3.jpg"
├─ variants: [
│   ├─ {color: "Đen",
│   │   ├─ detailImages: ["black-1.jpg", "black-2.jpg"]
│   │   └─ attributes: [
│   │       ├─ {id: "attr-001", name: "Size M", price: 250k}
│   │       └─ {id: "attr-002", name: "Size L", price: 250k}
│   │     ]
│   │  }
│   └─ {color: "Trắng",
│       ├─ detailImages: ["white-1.jpg", "white-2.jpg"]
│       └─ attributes: [
│           ├─ {id: "attr-003", name: "Size M", price: 250k}
│           └─ {id: "attr-004", name: "Size L", price: 250k}
│         ]
│      }
└─ ]
```

---

## 🎨 Styling Guide (CSS Tips)

### Home Page Grid
```css
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.product-card img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
}

.discount-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff4444;
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  font-weight: bold;
  font-size: 12px;
}

.price {
  display: flex;
  align-items: center;
  gap: 10px;
}

.final-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4444;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}
```

### Product Detail Page
```css
.product-detail-page {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.main-image {
  width: 100%;
  aspect-ratio: 1;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.thumbnail-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  margin-top: 15px;
}

.thumbnail {
  aspect-ratio: 1;
  object-fit: cover;
  cursor: pointer;
  border-radius: 4px;
  border: 2px solid transparent;
  transition: border-color 0.2s;
}

.thumbnail:hover {
  border-color: #ff4444;
}

.variant-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin: 10px 0;
}

.variant-btn {
  padding: 10px 20px;
  border: 2px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.variant-btn.active {
  border-color: #ff4444;
  background: #fff5f5;
  color: #ff4444;
}

.attribute-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin: 10px 0;
}

.attribute-btn {
  padding: 15px;
  border: 2px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
  transition: all 0.2s;
}

.attribute-btn:hover {
  border-color: #ff4444;
}

.attribute-btn.active {
  border-color: #ff4444;
  background: #fff5f5;
}

.add-to-cart-btn {
  width: 100%;
  padding: 15px;
  background: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 20px;
  transition: background 0.2s;
}

.add-to-cart-btn:hover:not(:disabled) {
  background: #ff2222;
}

.add-to-cart-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
```

---

## 💡 Best Practices

### 1. **Caching - Lưu Cache Dữ Liệu Home**
```javascript
// Lưu cache danh sách sản phẩm để tránh request quá nhiều
const CACHE_KEY = 'products_cache';
const CACHE_DURATION = 5 * 60 * 1000; // 5 phút

async function getProducts() {
  const cached = localStorage.getItem(CACHE_KEY);
  const cacheTime = localStorage.getItem(CACHE_KEY + '_time');

  if (cached && cacheTime && Date.now() - parseInt(cacheTime) < CACHE_DURATION) {
    return JSON.parse(cached);
  }

  const response = await fetch('http://localhost:8080/bej3/home');
  const data = await response.json();
  
  if (data.code === 1000) {
    localStorage.setItem(CACHE_KEY, JSON.stringify(data.result));
    localStorage.setItem(CACHE_KEY + '_time', Date.now().toString());
    return data.result;
  }
}
```

### 2. **Lazy Load Images**
```html
<!-- Sử dụng loading="lazy" cho ảnh trong grid -->
<img 
  src={product.image} 
  alt={product.name}
  loading="lazy"
/>
```

### 3. **Error Handling**
```javascript
async function fetchProduct(productId) {
  try {
    const response = await fetch(
      `http://localhost:8080/bej3/home/product/${productId}`
    );
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    
    if (data.code !== 1000) {
      throw new Error(data.message);
    }
    
    return data.result;
  } catch (error) {
    console.error('Lỗi tải sản phẩm:', error);
    // Hiển thị thông báo lỗi cho user
    return null;
  }
}
```

### 4. **Format Currency Properly**
```javascript
function formatPrice(price) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(price);
}

console.log(formatPrice(250000)); // "250.000 ₫"
```

### 5. **Calculate Discount Percentage**
```javascript
function calculateDiscount(originalPrice, finalPrice) {
  return Math.round(
    ((originalPrice - finalPrice) / originalPrice) * 100
  );
}

const discount = calculateDiscount(300000, 250000);
console.log(`Giảm ${discount}%`); // "Giảm 17%"
```

---

## 🧪 Cách Test API Bằng Postman

### 1. GET /home - Danh Sách Sản Phẩm
```
Method: GET
URL: http://localhost:8080/bej3/home
Headers: 
  - Content-Type: application/json
Body: (none)
```

### 2. GET /home/product/{productId} - Chi Tiết Sản Phẩm
```
Method: GET
URL: http://localhost:8080/bej3/home/product/product-001
Headers:
  - Content-Type: application/json
Body: (none)
```

**Postman Collection (JSON):**
```json
{
  "info": {
    "name": "Product API",
    "description": "API để lấy dữ liệu sản phẩm"
  },
  "item": [
    {
      "name": "Get All Products",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/bej3/home"
      }
    },
    {
      "name": "Get Product Details",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/bej3/home/product/{{productId}}"
      }
    }
  ]
}
```

---

## 📞 Error Handling Guide

### Error 404: Product Not Found
```javascript
if (data.code === 1004 || data.code === 1001) {
  console.error('Sản phẩm không tồn tại');
  window.location.href = '/404';
}
```

### Error 500: Server Error
```javascript
if (data.code === 500) {
  console.error('Lỗi server:', data.message);
  // Retry mechanism
  setTimeout(() => {
    location.reload();
  }, 3000);
}
```

---

## 🔗 Related APIs

- **Product Cart API**: Xem `CART_API_GUIDELINE.md` để tìm hiểu cách add sản phẩm vào giỏ
- **Category API**: `/admin/category` (Admin only)
- **Search API**: Sẽ được thêm trong tương lai

---

## 📈 Performance Tips

### 1. **Pagination (Nếu Có Nhiều Sản Phẩm)**
```javascript
// Tạo pagination khi số sản phẩm > 20
const ITEMS_PER_PAGE = 20;
const [currentPage, setCurrentPage] = useState(1);

const paginatedProducts = products.slice(
  (currentPage - 1) * ITEMS_PER_PAGE,
  currentPage * ITEMS_PER_PAGE
);
```

### 2. **Image Optimization**
```html
<!-- Sử dụng srcset để serve ảnh responsive -->
<img 
  src={product.image} 
  srcset={`${product.image}?w=280 280w, ${product.image}?w=560 560w`}
  sizes="(max-width: 600px) 280px, 560px"
  alt={product.name}
/>
```

### 3. **Request Debouncing (Khi Search)**
```javascript
function debounce(func, delay) {
  let timeoutId;
  return function(...args) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
}

const handleSearch = debounce((searchTerm) => {
  // Gọi API search
}, 500);
```

---

**Last Updated:** November 22, 2025  
**API Version:** 1.0.0  
**Status:** Production Ready

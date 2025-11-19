# Refactoring Summary - Repository-UseCase-StateFlow Pattern

## Tổng Quan
Đã chuẩn hóa toàn bộ kiến trúc codebase theo pattern clean architecture:
**Repository Interface → RepositoryImpl → UseCase → ViewModel (StateFlow) → Screen**

Tất cả response được wrap trong `Resource<T>` (Success, Error, Loading)

---

## Files Được Tạo Mới

### 1. Domain Layer - Repository Interface
📄 **`domain/repository/UserRepository.kt`** (NEW)
- Interface định nghĩa các operation user profile
- Methods: `getUserProfile()`, `updateUserProfile()`, `logout()`
- Tất cả trả về `Resource<T>`

### 2. Data Layer - Repository Implementation  
📄 **`data/repository/UserRepositoryImpl.kt`** (NEW)
- Implementation của UserRepository
- Gọi UserApiService để fetch/update profile
- Xử lý lỗi (401/403 logout user, network errors, etc.)
- Wrap tất cả response trong Resource<T>

### 3. Domain Layer - UseCase
📄 **`domain/usecase/HomeUseCase.kt`** (NEW)
- Orchestrate fetching sản phẩm home
- Validation logic nếu cần

📄 **`domain/usecase/UpdateUserProfileUseCase.kt`** (NEW)
- Handle validation + call UserRepository.updateUserProfile()

📄 **`domain/usecase/GetUserProfileUseCase.kt`** (UPDATED)
- Thay thế mock data → gọi UserRepository.getUserProfile()

---

## Files Được Cập Nhật

### 4. Domain Layer - Repository Interface
📄 **`domain/repository/ProductRepository.kt`** 
- BEFORE: Trả về `HomeResponse` (raw response model)
- AFTER: Trả về `Resource<List<Product>>` (wrapped + processed)

### 5. Data Layer - Repository Implementation
📄 **`data/repository/ProductRepositoryImpl.kt`**
- BEFORE: Ném exception, caller phải handle try-catch
- AFTER: Wrap mọi thứ trong Resource<T>, xử lý tất cả errors

### 6. Presentation Layer - ViewModels
📄 **`presentation/viewmodel/HomeViewModel.kt`**
- BEFORE: 3 separate flows (products, isLoading, error) + direct repository call
- AFTER: Single `homeState` StateFlow + HomeUseCase
```kotlin
// Before
val products: StateFlow<List<Product>>
val isLoading: StateFlow<Boolean>
val error: StateFlow<String?>

// After
val homeState: StateFlow<HomeState>
data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

📄 **`presentation/viewmodel/ProfileViewModel.kt`**
- BEFORE: Direct UserApiService calls + PreferenceDataSource
- AFTER: GetUserProfileUseCase + UserRepository pattern

📄 **`presentation/viewmodel/EditProfileViewModel.kt`**
- BEFORE: Direct UserApiService + complex error handling
- AFTER: GetUserProfileUseCase + UpdateUserProfileUseCase + clean error handling

### 7. Presentation Layer - Screens
📄 **`presentation/screen/HomeScreen.kt`**
```kotlin
// Before
val products = viewModel.products.collectAsState()
val isLoading = viewModel.isLoading.collectAsState()
val error = viewModel.error.collectAsState()

// After
val homeState = viewModel.homeState.collectAsState()
val state = homeState.value
```

### 8. Dependency Injection
📄 **`di/RepositoryModule.kt`**
- Thêm binding: `UserRepository` → `UserRepositoryImpl`

---

## Architecture Flow - Before & After

### BEFORE (Mixed patterns)
```
HomeScreen
   ↓
HomeViewModel (3 separate flows)
   ↓
ProductRepository (direct call)
   ↓ (throws exceptions)
API
```

### AFTER (Clean Architecture)
```
HomeScreen (collect homeState)
   ↓
HomeViewModel (single homeState StateFlow)
   ↓
HomeUseCase (validation + orchestration)
   ↓
ProductRepository Interface
   ↓
ProductRepositoryImpl (Resource<T> wrapping)
   ↓
API
```

---

## Key Benefits

✅ **Consistent Pattern**: Tất cả ViewModel follow cùng một pattern
✅ **Error Handling**: Centralized trong Repository layer
✅ **Resource Wrapper**: Tất cả response đều có Loading/Success/Error
✅ **Testability**: Dễ mock Repository/UseCase cho unit tests
✅ **Maintainability**: Rõ ràng flow của data từ API → Screen
✅ **Type Safety**: Compile-time type checking cho tất cả data

---

## Danh Sách Files Liên Quan

### Created:
- ✨ `domain/repository/UserRepository.kt`
- ✨ `data/repository/UserRepositoryImpl.kt`
- ✨ `domain/usecase/HomeUseCase.kt`
- ✨ `domain/usecase/UpdateUserProfileUseCase.kt`

### Modified:
- 📝 `domain/repository/ProductRepository.kt`
- 📝 `data/repository/ProductRepositoryImpl.kt`
- 📝 `presentation/viewmodel/HomeViewModel.kt`
- 📝 `presentation/viewmodel/ProfileViewModel.kt`
- 📝 `presentation/viewmodel/EditProfileViewModel.kt`
- 📝 `presentation/screen/HomeScreen.kt`
- 📝 `domain/usecase/GetUserProfileUseCase.kt`
- 📝 `di/RepositoryModule.kt`

### No Changes Needed:
- ✓ `presentation/viewmodel/LoginViewModel.kt` (Already perfect)
- ✓ `presentation/viewmodel/RegisterViewModel.kt` (Already perfect)
- ✓ `presentation/screen/LoginScreen.kt` (Already perfect)
- ✓ `presentation/screen/ProfileScreen.kt` (Already correct structure)
- ✓ `presentation/screen/EditProfileScreen.kt` (Already correct structure)

---

## Testing

Để test xem tất cả đã work:

1. **Clean Build**: `./gradlew clean build`
2. **Run App**: Và kiểm tra:
   - ✅ Home screen load products
   - ✅ Profile screen load user info
   - ✅ Edit profile có thể update
   - ✅ Error handling hoạt động (no network, 401, etc.)

---

## Notes

- Repository layer giờ chịu trách nhiệm handling tất cả network errors
- UseCase layer chỉ làm validation logic
- ViewModel chỉ update state dựa trên UseCase kết quả
- Screen chỉ đọc state và hiển thị

Cấu trúc này dễ mở rộng và bảo trì khi thêm features mới!


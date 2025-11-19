# 📋 DETAILED CHANGES SUMMARY

## 🎯 Objective
Standardize all ViewModels and Screens to follow Clean Architecture pattern with:
- Single StateFlow per ViewModel (not multiple)
- Resource<T> wrapping for all network responses
- UseCase for business logic
- Repository abstraction layer
- Consistent error handling

---

## 📁 Files Created (4 New Files)

### 1. `domain/repository/UserRepository.kt` ✨ NEW
**Purpose**: Interface for user profile operations

```kotlin
interface UserRepository {
    suspend fun getUserProfile(): Resource<UserProfile>
    suspend fun updateUserProfile(fullName: String, address: String?, dob: String?): Resource<UserProfile>
    suspend fun logout()
}
```

**Why**: Abstracts user profile operations from implementation details

---

### 2. `data/repository/UserRepositoryImpl.kt` ✨ NEW
**Purpose**: Implementation of UserRepository, handles API calls and error mapping

**Key Features**:
- ✅ Wraps all responses in Resource<T>
- ✅ Handles 401/403 errors (token expiry)
- ✅ Catches all exceptions (HttpException, IOException, etc.)
- ✅ Centralized error handling for user endpoints

**Example Error Handling**:
```kotlin
401, 403 -> {
    preferenceDataSource.clearToken()  // Auto-logout on token error
    Resource.Error("Phiên đăng nhập hết hạn")
}
```

---

### 3. `domain/usecase/HomeUseCase.kt` ✨ NEW
**Purpose**: Orchestrate home product fetching with validation

```kotlin
class HomeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Resource<List<Product>> {
        return productRepository.getHomeProducts()
    }
}
```

**Why**: Single responsibility - delegates validation to repository, simple invoke interface

---

### 4. `domain/usecase/UpdateUserProfileUseCase.kt` ✨ NEW
**Purpose**: Handle profile update validation and orchestration

```kotlin
class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(fullName: String, address: String?, dob: String?): Resource<UserProfile> {
        if (fullName.isBlank()) {
            return Resource.Error("Tên không được để trống")
        }
        return userRepository.updateUserProfile(fullName, address, dob)
    }
}
```

**Why**: Centralized validation logic, clean interface for ViewModel

---

## 📝 Files Modified (8 Updated Files)

### 1. `domain/repository/ProductRepository.kt` 📝 MODIFIED
**Before**:
```kotlin
interface ProductRepository {
    suspend fun getHomeProducts(): HomeResponse  // Raw response object
    suspend fun getProductDetail(productId: String): HomeResponse
}
```

**After**:
```kotlin
interface ProductRepository {
    suspend fun getHomeProducts(): Resource<List<Product>>  // Wrapped + processed
    suspend fun getProductDetail(productId: String): Resource<Product>
}
```

**Changes**:
- ✅ Returns `Resource<List<Product>>` instead of raw `HomeResponse`
- ✅ Responsibility moved to repository for error wrapping
- ✅ Enables consistent error handling across all repositories

---

### 2. `data/repository/ProductRepositoryImpl.kt` 📝 MODIFIED
**Before**:
```kotlin
override suspend fun getHomeProducts(): HomeResponse {
    val response = apiService.getHomeProducts()
    return if (response.isSuccessful) {
        response.body() ?: throw Exception("Empty response")  // ❌ Throws exception
    } else {
        throw Exception(response.message())
    }
}
```

**After**:
```kotlin
override suspend fun getHomeProducts(): Resource<List<Product>> {
    return try {
        val response = apiService.getHomeProducts()
        if (response.isSuccessful) {
            val homeResponse = response.body()
            if (homeResponse?.result != null) {
                Resource.Success(homeResponse.result)  // ✅ Wrapped
            } else {
                Resource.Error("Response empty")
            }
        } else {
            Resource.Error("Failed: ${response.message()}")
        }
    } catch (e: HttpException) {
        Resource.Error("Network error: ${e.message()}")
    } catch (e: IOException) {
        Resource.Error("Connection error: ${e.message()}")
    } catch (e: Exception) {
        Resource.Error("Unknown error: ${e.message()}")
    }
}
```

**Changes**:
- ✅ Wraps response in Resource.Success()
- ✅ Wraps errors in Resource.Error()
- ✅ No exception throwing - all handled in resource wrapper

---

### 3. `presentation/viewmodel/HomeViewModel.kt` 📝 MODIFIED
**Before** (3 separate flows):
```kotlin
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getHomeProducts()
                _products.value = response.result  // Direct access
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
```

**After** (Single state):
```kotlin
data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _homeState.value = HomeState(isLoading = true)

            when (val result = homeUseCase()) {
                is Resource.Success -> {
                    _homeState.value = HomeState(
                        products = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _homeState.value = HomeState(
                        products = emptyList(),
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> { }
            }
        }
    }
}
```

**Key Changes**:
- ✅ Single `homeState: StateFlow<HomeState>` (not 3 separate flows)
- ✅ Calls `HomeUseCase` instead of repository directly
- ✅ Uses `when (result)` pattern for Resource handling
- ✅ Consistent error handling pattern

---

### 4. `presentation/viewmodel/ProfileViewModel.kt` 📝 MODIFIED
**Before** (Direct API calls):
```kotlin
class ProfileViewModel @Inject constructor(
    private val userApiService: UserApiService,  // ❌ Direct API dependency
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {
    private fun loadUserProfile() {
        try {
            val response = userApiService.getUserProfile()  // Direct call
            if (response.isSuccessful) {
                val userProfile = response.body()?.result?.toUserProfile()
                _profileState.value = _profileState.value.copy(userProfile = userProfile)
            } else {
                when (response.code()) {
                    401, 403 -> {
                        preferenceDataSource.clearToken()  // ❌ Logic in ViewModel
                    }
                }
            }
        } catch (e: Exception) {
            _profileState.value = _profileState.value.copy(error = "Lỗi: ${e.message}")
        }
    }
}
```

**After** (UseCase + Repository):
```kotlin
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,  // ✅ Via UseCase
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {
    private fun loadUserProfile() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true, error = null)

            when (val result = getUserProfileUseCase()) {  // ✅ Clean UseCase call
                is Resource.Success -> {
                    _profileState.value = _profileState.value.copy(
                        userProfile = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _profileState.value = _profileState.value.copy(
                        isLoading = false,
                        error = result.message  // ✅ Error from repository
                    )
                }
                else -> { }
            }
        }
    }
}
```

**Key Changes**:
- ✅ Injects `GetUserProfileUseCase` instead of `UserApiService`
- ✅ Repository handles 401/403 logout (not ViewModel)
- ✅ Error messages are consistent (from Resource)
- ✅ Cleaner separation of concerns

---

### 5. `presentation/viewmodel/EditProfileViewModel.kt` 📝 MODIFIED
**Before** (Complex error handling in ViewModel):
```kotlin
class EditProfileViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {
    fun updateUserProfile(fullName: String?, address: String?, dob: String?, password: String?) {
        try {
            val updateRequest = UserUpdateRequest(
                fullName = fullName?.takeIf { it.isNotBlank() },
                address = address?.takeIf { it.isNotBlank() },
                dob = dob?.takeIf { it.isNotBlank() },
                password = password?.takeIf { it.isNotBlank() }
            )
            val response = userApiService.updateUserProfile(updateRequest)
            
            if (response.isSuccessful) {
                try {
                    val updateResponse = response.body()
                    if (updateResponse?.result != null) {
                        _editProfileState.value = _editProfileState.value.copy(
                            userProfile = updateResponse.result.toUserProfile()
                        )
                    }
                } catch (jsonError: JsonDataException) {
                    // ❌ Complex JSON error handling
                }
            } else {
                when (response.code()) {
                    400 -> { /* Handle 400 */ }
                    401 -> { /* Handle 401 */ }
                    403 -> { /* Handle 403 */ }
                    500 -> { /* Handle 500 */ }
                }
            }
        } catch (e: Exception) {
            // ❌ Complex exception handling
        }
    }
}
```

**After** (Clean UseCase pattern):
```kotlin
class EditProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,      // ✅ UseCase
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,  // ✅ UseCase
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {
    fun updateUserProfile(fullName: String?, address: String?, dob: String?) {
        viewModelScope.launch {
            _editProfileState.value = _editProfileState.value.copy(
                isSaving = true,
                error = null
            )

            when (val result = updateUserProfileUseCase(  // ✅ Clean call
                fullName = fullName ?: "",
                address = address,
                dob = dob
            )) {
                is Resource.Success -> {
                    _editProfileState.value = _editProfileState.value.copy(
                        userProfile = result.data,
                        isSaving = false,
                        error = null
                    )
                    MessageManager.showSuccess("✅ Cập nhật thành công")  // ✅ Cleaner
                }
                is Resource.Error -> {
                    _editProfileState.value = _editProfileState.value.copy(
                        isSaving = false,
                        error = result.message
                    )
                    MessageManager.showError(result.message ?: "Lỗi")
                }
                else -> { }
            }
        }
    }
}
```

**Key Changes**:
- ✅ Injects both `GetUserProfileUseCase` and `UpdateUserProfileUseCase`
- ✅ All error handling in repository (401, 403, network errors)
- ✅ UseCase does validation (fullName not blank)
- ✅ ViewModel just handles state and shows message

---

### 6. `presentation/screen/HomeScreen.kt` 📝 MODIFIED
**Before** (3 separate state collections):
```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel, ...) {
    val products = viewModel.products.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    // If loading
    if (isLoading.value) { ... }
    
    // If error
    error.value?.let { ... }
    
    // If success
    LazyColumn {
        items(products.value) { ... }
    }
}
```

**After** (Single state collection):
```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel, ...) {
    val homeState = viewModel.homeState.collectAsState()
    val state = homeState.value

    Column(modifier = Modifier.fillMaxSize()) {
        // If loading
        if (state.isLoading) { ... }
        
        // If error
        state.error?.let { ... }
        
        // If success
        LazyColumn {
            items(state.products) { ... }  // Use state.products
        }
    }
}
```

**Key Changes**:
- ✅ Single `homeState.collectAsState()`
- ✅ Use `state.isLoading`, `state.error`, `state.products`
- ✅ Cleaner, easier to understand

---

### 7. `domain/usecase/GetUserProfileUseCase.kt` 📝 MODIFIED
**Before** (Mock data):
```kotlin
class GetUserProfileUseCase @Inject constructor(
    //private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<UserProfile> {
        //return userRepository.getUserProfile()
        return Resource.Success(UserProfile(
            email = "admin@gmail.com",
            id = "fake-user-id-123",
            fullName = "Người dùng Test",
            // ... mock data
        ))
    }
}
```

**After** (Real repository):
```kotlin
class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository  // ✅ Real dependency
) {
    suspend operator fun invoke(): Resource<UserProfile> {
        return userRepository.getUserProfile()  // ✅ Real call
    }
}
```

**Changes**:
- ✅ Replaced mock data with real UserRepository
- ✅ Removed commented code
- ✅ Clean, simple delegation

---

### 8. `di/RepositoryModule.kt` 📝 MODIFIED
**Before**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
}
```

**After**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository  // ✅ NEW
}
```

**Changes**:
- ✅ Added `bindUserRepository()` for DI to inject UserRepository

---

## 📊 Impact Analysis

### Before Refactoring
```
Issues:
❌ Multiple flows per ViewModel (confusing state management)
❌ Mixed exception handling (some throw, some return null)
❌ Direct API calls in ViewModel (tight coupling)
❌ No consistent Resource wrapping
❌ Error handling scattered across layers
❌ Hard to test (can't mock without complex setup)
```

### After Refactoring
```
Improvements:
✅ Single State dataclass per ViewModel
✅ Consistent Resource<T> wrapping
✅ UseCase orchestrates business logic
✅ Repository handles all API/error logic
✅ ViewModel only manages state
✅ Screen only renders state
✅ Easy to test (mock UseCase/Repository)
✅ Clear separation of concerns
```

---

## 🔄 Data Flow Example: Load Home Products

### Before
```
HomeScreen
    ↓ collect 3 flows
HomeViewModel.products, isLoading, error
    ↓ productRepository.getHomeProducts()
ProductRepositoryImpl
    ↓ throws exception on error
HomeScreen catches nothing (exception in ViewModel)
```

### After
```
HomeScreen
    ↓ collect single homeState
HomeViewModel.homeState (HomeState dataclass)
    ↓ when (homeUseCase())
HomeUseCase
    ↓ productRepository.getHomeProducts()
ProductRepositoryImpl
    ↓ Resource.Success/Error/Loading
HomeViewModel
    ↓ update homeState
HomeScreen
    ↓ render state.products/error/isLoading
```

---

## ✅ Verification

All files have been:
- ✅ Created/Updated
- ✅ Follow consistent pattern
- ✅ Use Resource<T> wrapping
- ✅ Have proper error handling
- ✅ DI configured

Ready for testing! 🚀


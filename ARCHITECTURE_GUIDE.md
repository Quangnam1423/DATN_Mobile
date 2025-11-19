# Hướng Dẫn Cấu Trúc Mới - Clean Architecture Pattern

## 📋 Quick Reference

### Pattern Chuẩn Được Sử Dụng

Tất cả ViewModels, UseCase, và Repository đều theo cấu trúc này:

```
┌─────────────────────────────────────────┐
│        Composable Screen                │
│  (Collect state & render UI)            │
└────────────┬────────────────────────────┘
             │ collect homeState
             ▼
┌─────────────────────────────────────────┐
│        ViewModel                        │
│  - Single StateFlow<State>              │
│  - State = data class                   │
│  - Call UseCase to load data            │
└────────────┬────────────────────────────┘
             │ when (result)
             ▼
┌─────────────────────────────────────────┐
│        UseCase (Business Logic)         │
│  - Validation logic                     │
│  - Call Repository                      │
│  - Return Resource<T>                   │
└────────────┬────────────────────────────┘
             │ repository.getData()
             ▼
┌─────────────────────────────────────────┐
│        Repository Interface             │
│  (Abstraction layer)                    │
└────────────┬────────────────────────────┘
             │ inject
             ▼
┌─────────────────────────────────────────┐
│        RepositoryImpl                    │
│  - Call API Service                     │
│  - Error handling & wrapping            │
│  - Return Resource<T>                   │
└────────────┬────────────────────────────┘
             │ api.call()
             ▼
┌─────────────────────────────────────────┐
│        API Service / Local DB           │
│  (External data source)                 │
└─────────────────────────────────────────┘
```

---

## 🎯 Áp Dụng Pattern Cho Feature Mới

### Step 1: Tạo Domain Model (nếu chưa có)
```kotlin
// domain/model/YourModel.kt
data class YourModel(
    val id: String,
    val name: String,
    // ... other fields
)
```

### Step 2: Tạo Repository Interface
```kotlin
// domain/repository/YourRepository.kt
interface YourRepository {
    suspend fun getData(): Resource<List<YourModel>>
    suspend fun getDetail(id: String): Resource<YourModel>
    suspend fun saveData(model: YourModel): Resource<Unit>
}
```

### Step 3: Tạo RepositoryImpl
```kotlin
// data/repository/YourRepositoryImpl.kt
@Singleton
class YourRepositoryImpl @Inject constructor(
    private val yourApiService: YourApiService
) : YourRepository {
    override suspend fun getData(): Resource<List<YourModel>> {
        return try {
            val response = yourApiService.getData()
            if (response.isSuccessful) {
                val data = response.body()?.result
                if (data != null) {
                    Resource.Success(data)
                } else {
                    Resource.Error("Data is null")
                }
            } else {
                Resource.Error("Failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")
        }
    }
    
    // ... other methods
}
```

### Step 4: Tạo UseCase
```kotlin
// domain/usecase/GetYourDataUseCase.kt
class GetYourDataUseCase @Inject constructor(
    private val yourRepository: YourRepository
) {
    suspend operator fun invoke(): Resource<List<YourModel>> {
        // Add validation if needed
        if (someCondition) {
            return Resource.Error("Validation failed")
        }
        return yourRepository.getData()
    }
}
```

### Step 5: Tạo State Data Class
```kotlin
// presentation/viewmodel/YourState.kt
data class YourState(
    val items: List<YourModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedItem: YourModel? = null
)
```

### Step 6: Tạo ViewModel
```kotlin
// presentation/viewmodel/YourViewModel.kt
@HiltViewModel
class YourViewModel @Inject constructor(
    private val getYourDataUseCase: GetYourDataUseCase
) : ViewModel() {
    private val _yourState = MutableStateFlow(YourState())
    val yourState = _yourState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _yourState.value = YourState(isLoading = true)
            
            when (val result = getYourDataUseCase()) {
                is Resource.Success -> {
                    _yourState.value = YourState(
                        items = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _yourState.value = YourState(
                        items = emptyList(),
                        isLoading = false,
                        error = result.message
                    )
                }
                else -> {}
            }
        }
    }
}
```

### Step 7: Tạo Screen
```kotlin
// presentation/screen/YourScreen.kt
@Composable
fun YourScreen(
    viewModel: YourViewModel = hiltViewModel()
) {
    val yourState by viewModel.yourState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        when {
            yourState.isLoading -> {
                CircularProgressIndicator()
            }
            yourState.error != null -> {
                Text("Error: ${yourState.error}", color = Color.Red)
                Button(onClick = { viewModel.loadData() }) {
                    Text("Retry")
                }
            }
            else -> {
                LazyColumn {
                    items(yourState.items) { item ->
                        YourItemCard(item)
                    }
                }
            }
        }
    }
}
```

### Step 8: Cập Nhật DI Module
```kotlin
// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // ... existing bindings
    
    @Binds
    @Singleton
    abstract fun bindYourRepository(
        yourRepositoryImpl: YourRepositoryImpl
    ) : YourRepository
}
```

---

## 🔍 Danh Sách So Sánh - Pattern Cũ vs Mới

### HomeViewModel
```kotlin
// ❌ CŨ
class HomeViewModel(private val productRepository: ProductRepository) {
    val products: StateFlow<List<Product>>
    val isLoading: StateFlow<Boolean>
    val error: StateFlow<String?>
    
    fun loadProducts() {
        try {
            val response = productRepository.getHomeProducts()
            _products.value = response.result  // Direct access
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}

// ✅ MỚI
data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val homeUseCase: HomeUseCase) {
    val homeState: StateFlow<HomeState>
    
    fun loadProducts() {
        when (val result = homeUseCase()) {
            is Resource.Success -> { /* update state */ }
            is Resource.Error -> { /* update error */ }
            else -> {}
        }
    }
}
```

### ProfileViewModel
```kotlin
// ❌ CŨ
class ProfileViewModel(
    private val userApiService: UserApiService,
    private val preferenceDataSource: PreferenceDataSource
) {
    private fun loadUserProfile() {
        try {
            val response = userApiService.getUserProfile()  // Direct API call
            if (response.isSuccessful) {
                val userProfile = response.body()?.result?.toUserProfile()
                _profileState.value = _profileState.value.copy(userProfile = userProfile)
            }
        } catch (e: Exception) {
            // Error handling
        }
    }
}

// ✅ MỚI
class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val preferenceDataSource: PreferenceDataSource
) {
    private fun loadUserProfile() {
        when (val result = getUserProfileUseCase()) {  // Via UseCase
            is Resource.Success -> {
                _profileState.value = _profileState.value.copy(
                    userProfile = result.data
                )
            }
            is Resource.Error -> { /* centralized error handling */ }
            else -> {}
        }
    }
}
```

---

## 🛠️ Common Tasks

### Thêm Pagination để Home Products
```kotlin
// domain/usecase/GetHomeProductsUseCase.kt
class GetHomeProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): Resource<List<Product>> {
        if (page < 0 || pageSize <= 0) {
            return Resource.Error("Invalid page or pageSize")
        }
        return productRepository.getHomeProducts()
    }
}

// presentation/viewmodel/HomeViewModel.kt
data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)

fun loadMore() {
    if (!homeState.value.hasMore) return
    loadProducts(homeState.value.currentPage + 1)
}
```

### Thêm Search Function
```kotlin
// domain/repository/YourRepository.kt
interface YourRepository {
    suspend fun search(query: String): Resource<List<YourModel>>
}

// domain/usecase/SearchUseCase.kt
class SearchUseCase @Inject constructor(
    private val yourRepository: YourRepository
) {
    suspend operator fun invoke(query: String): Resource<List<YourModel>> {
        if (query.isBlank()) {
            return Resource.Error("Search query cannot be empty")
        }
        return yourRepository.search(query)
    }
}

// presentation/viewmodel/SearchViewModel.kt
fun onSearchQueryChanged(query: String) {
    viewModelScope.launch {
        _searchState.value = _searchState.value.copy(
            isLoading = true,
            error = null
        )
        
        val result = searchUseCase(query)
        when (result) {
            is Resource.Success -> {
                _searchState.value = _searchState.value.copy(
                    results = result.data ?: emptyList(),
                    isLoading = false
                )
            }
            is Resource.Error -> {
                _searchState.value = _searchState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
            else -> {}
        }
    }
}
```

---

## ⚠️ Common Mistakes to Avoid

### ❌ Sai: Gọi API trực tiếp trong ViewModel
```kotlin
class MyViewModel : ViewModel() {
    fun load() {
        viewModelScope.launch {
            val response = apiService.getData()  // ❌ WRONG
        }
    }
}
```

### ✅ Đúng: Gọi qua UseCase
```kotlin
class MyViewModel(private val myUseCase: MyUseCase) : ViewModel() {
    fun load() {
        viewModelScope.launch {
            val result = myUseCase()  // ✅ RIGHT
        }
    }
}
```

### ❌ Sai: Nhiều StateFlow riêng lẻ
```kotlin
class MyViewModel {
    val data: StateFlow<List<Item>>
    val isLoading: StateFlow<Boolean>
    val error: StateFlow<String?>
    val selectedItem: StateFlow<Item?>
    // ... 10 more flows
}
```

### ✅ Đúng: Một State dataclass
```kotlin
data class MyState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedItem: Item? = null
)

class MyViewModel {
    val myState: StateFlow<MyState>
}
```

### ❌ Sai: Không wrap response
```kotlin
class MyRepositoryImpl : MyRepository {
    override suspend fun getData(): List<Item> {
        return try {
            apiService.getData().body()?.result ?: emptyList()
        } catch (e: Exception) {
            throw e  // ❌ Ném exception
        }
    }
}
```

### ✅ Đúng: Wrap trong Resource
```kotlin
class MyRepositoryImpl : MyRepository {
    override suspend fun getData(): Resource<List<Item>> {
        return try {
            val response = apiService.getData()
            if (response.isSuccessful) {
                Resource.Success(response.body()?.result ?: emptyList())
            } else {
                Resource.Error("Failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")  // ✅ Wrap lỗi
        }
    }
}
```

---

## 📚 Files Tham Khảo

### Đã Áp Dụng Pattern (Làm Tham Khảo)
- ✅ LoginViewModel & LoginScreen (Perfect example)
- ✅ RegisterViewModel
- ✅ HomeViewModel & HomeScreen (mới refactor)
- ✅ ProfileViewModel & ProfileScreen (mới refactor)
- ✅ EditProfileViewModel & EditProfileScreen (mới refactor)

### Files Resource
- 📄 `data/util/Resource.kt` - Xem cách dùng Resource<T>
- 📄 `domain/usecase/LoginUseCase.kt` - UseCase reference
- 📄 `data/repository/AuthRepositoryImpl.kt` - Repository reference

---

## 🚀 Testing

### Unit Test UseCase
```kotlin
@Test
fun testGetUserProfileUseCase_Success() = runTest {
    val mockRepository = mockk<UserRepository>()
    val userProfile = UserProfile(/* ... */)
    
    coEvery { mockRepository.getUserProfile() } returns Resource.Success(userProfile)
    
    val useCase = GetUserProfileUseCase(mockRepository)
    val result = useCase()
    
    assert(result is Resource.Success)
    assert((result as Resource.Success).data == userProfile)
}
```

### Unit Test ViewModel
```kotlin
@Test
fun testHomeViewModel_LoadProducts_Success() = runTest {
    val mockUseCase = mockk<HomeUseCase>()
    val products = listOf(Product(/* ... */))
    
    coEvery { mockUseCase() } returns Resource.Success(products)
    
    val viewModel = HomeViewModel(mockUseCase)
    advanceUntilIdle()
    
    assert(viewModel.homeState.value.products == products)
    assert(viewModel.homeState.value.error == null)
}
```

---

## 📞 Support

Nếu có câu hỏi về pattern:
1. Xem LoginViewModel/LoginScreen làm reference (most perfect)
2. Xem tệp này để hiểu flow
3. Kiểm tra test files để hiểu cách test

Happy coding! 🎉


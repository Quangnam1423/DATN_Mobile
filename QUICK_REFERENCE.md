# 🚀 QUICK REFERENCE CARD

## Architecture Pattern

```
Screen (Render state)
  ↓ collect state
ViewModel (StateFlow<State>)
  ↓ when (result)
UseCase (Business logic)
  ↓ call function
Repository Interface
  ↓ inject
RepositoryImpl (API + Error handling)
  ↓ api.call()
API Service / Database
```

---

## Single State Pattern

### ❌ DON'T DO THIS
```kotlin
val data: StateFlow<List<Item>>
val isLoading: StateFlow<Boolean>
val error: StateFlow<String?>
val selected: StateFlow<Item?>
```

### ✅ DO THIS
```kotlin
data class State(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selected: Item? = null
)

val state: StateFlow<State>
```

---

## Resource Wrapper

### ✅ Always Return Resource<T>

**In Repository**:
```kotlin
override suspend fun getData(): Resource<List<Item>> {
    return try {
        val response = apiService.getData()
        if (response.isSuccessful) {
            Resource.Success(response.body()?.result ?: emptyList())
        } else {
            Resource.Error("Failed: ${response.message()}")
        }
    } catch (e: Exception) {
        Resource.Error("Error: ${e.message}")
    }
}
```

**In ViewModel**:
```kotlin
when (val result = useCase()) {
    is Resource.Success -> updateUI(result.data)
    is Resource.Error -> showError(result.message)
    is Resource.Loading -> showLoading()
}
```

---

## Error Handling in Repository

### Handle Common HTTP Errors
```kotlin
when (response.code()) {
    401, 403 -> {
        // Token expired - auto logout
        preferenceDataSource.clearToken()
        Resource.Error("Phiên hết hạn")
    }
    500 -> Resource.Error("Server error")
    else -> Resource.Error("Failed: ${response.message()}")
}
```

### Handle Network Errors
```kotlin
catch (e: HttpException) {
    Resource.Error("Network error: ${e.message()}")
} catch (e: IOException) {
    Resource.Error("Connection error: ${e.message()}")
} catch (e: Exception) {
    Resource.Error("Unknown error: ${e.message()}")
}
```

---

## ViewModel Template

```kotlin
data class MyState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myUseCase: MyUseCase
) : ViewModel() {
    private val _myState = MutableStateFlow(MyState())
    val myState = _myState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _myState.value = MyState(isLoading = true)
            
            when (val result = myUseCase()) {
                is Resource.Success -> {
                    _myState.value = MyState(items = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _myState.value = MyState(error = result.message)
                }
                else -> {}
            }
        }
    }
}
```

---

## Screen Template

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val myState by viewModel.myState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        when {
            myState.isLoading -> {
                CircularProgressIndicator()
            }
            myState.error != null -> {
                Text("Error: ${myState.error}", color = Color.Red)
                Button(onClick = { viewModel.load() }) {
                    Text("Retry")
                }
            }
            else -> {
                LazyColumn {
                    items(myState.items) { item ->
                        ItemCard(item)
                    }
                }
            }
        }
    }
}
```

---

## UseCase Template

```kotlin
class MyUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(): Resource<List<Item>> {
        // Validation
        if (someCondition) {
            return Resource.Error("Validation failed")
        }
        // Delegate to repository
        return myRepository.getItems()
    }
}
```

---

## Repository Interface Template

```kotlin
interface MyRepository {
    suspend fun getItems(): Resource<List<Item>>
    suspend fun getItem(id: String): Resource<Item>
    suspend fun saveItem(item: Item): Resource<Unit>
}
```

---

## RepositoryImpl Template

```kotlin
@Singleton
class MyRepositoryImpl @Inject constructor(
    private val myApiService: MyApiService
) : MyRepository {
    override suspend fun getItems(): Resource<List<Item>> {
        return try {
            val response = myApiService.getItems()
            if (response.isSuccessful) {
                val items = response.body()?.result
                if (items != null) {
                    Resource.Success(items)
                } else {
                    Resource.Error("Empty response")
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
}
```

---

## DI Binding Template

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMyRepository(
        myRepositoryImpl: MyRepositoryImpl
    ) : MyRepository
}
```

---

## State Update Pattern

```kotlin
// ❌ Don't do this
_state.value = state.copy(
    isLoading = false,
    error = null,
    items = result.data
)

// ✅ Do this - Reset state properly
_state.value = MyState(
    items = result.data ?: emptyList(),
    isLoading = false,
    error = null
)
```

---

## Common State Fields

```kotlin
data class StandardState(
    val data: List<Item> = emptyList(),        // Your main data
    val isLoading: Boolean = false,            // Loading indicator
    val error: String? = null,                 // Error message
    val isAuthenticated: Boolean = false,      // Auth check
    val isSaving: Boolean = false,             // Form submit
    val selectedItem: Item? = null,            // User selection
    val currentPage: Int = 0                   // Pagination
)
```

---

## Validation in UseCase

```kotlin
class MyUseCase @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(input: String): Resource<Result> {
        // Validate input
        if (input.isBlank()) {
            return Resource.Error("Input không được để trống")
        }
        
        if (input.length < 5) {
            return Resource.Error("Input phải >= 5 ký tự")
        }
        
        // Call repository
        return myRepository.processData(input)
    }
}
```

---

## Testing

### Mock UseCase
```kotlin
val mockUseCase = mockk<MyUseCase>()
coEvery { mockUseCase() } returns Resource.Success(listOf(...))

val viewModel = MyViewModel(mockUseCase)
```

### Test ViewModel
```kotlin
@Test
fun testLoad_Success() = runTest {
    val mockUseCase = mockk<MyUseCase>()
    coEvery { mockUseCase() } returns Resource.Success(listOf(item))
    
    val viewModel = MyViewModel(mockUseCase)
    advanceUntilIdle()
    
    assert(viewModel.myState.value.items == listOf(item))
    assert(viewModel.myState.value.error == null)
}
```

---

## Common Issues

### Issue: Multiple setState calls
```kotlin
// ❌ Wrong - overwrites previous state
_state.value = State(isLoading = true)
when (result) {
    is Resource.Success -> _state.value = State(data = result.data)  // isLoading is lost!
}

// ✅ Right - preserve current state
_state.value = State(isLoading = true)
when (result) {
    is Resource.Success -> _state.value = _state.value.copy(
        data = result.data,
        isLoading = false
    )
}
```

### Issue: Direct API call in ViewModel
```kotlin
// ❌ Wrong
class ViewModel {
    fun load() {
        val response = apiService.getData()  // Direct call
    }
}

// ✅ Right
class ViewModel(private val useCase: UseCase) {
    fun load() {
        val result = useCase()  // Via UseCase
    }
}
```

### Issue: No error handling
```kotlin
// ❌ Wrong
val result = repository.getData()  // What if error?

// ✅ Right
when (val result = useCase()) {
    is Resource.Success -> {}
    is Resource.Error -> {}
    is Resource.Loading -> {}
}
```

---

## Files Location Reference

```
app/src/main/java/com/example/datn_mobile/

├── domain/
│   ├── model/              ← Domain models
│   ├── repository/         ← Repository interfaces
│   └── usecase/            ← Business logic
│
├── data/
│   ├── repository/         ← Repository implementations
│   ├── network/api/        ← API Services
│   └── util/               ← Resource<T> class
│
├── presentation/
│   ├── viewmodel/          ← ViewModels + State classes
│   └── screen/             ← Composable screens
│
└── di/
    └── *.kt                ← Dependency Injection
```

---

## Done ✅

You have:
- ✅ 4 new files created
- ✅ 8 files refactored
- ✅ Consistent pattern applied
- ✅ Error handling centralized
- ✅ Ready for production

Now go build awesome features! 🚀


# Checklist - Other ViewModels Status

## Current Status của Tất Cả ViewModels

### ✅ COMPLETED & REFACTORED

#### 1. LoginViewModel
- **Status**: ✅ Perfect (Reference Model)
- **Pattern**: ✅ Uses LoginUseCase + LoginState StateFlow + Resource<T>
- **Screen**: LoginScreen (already correct)
- **Files**: 
  - `domain/usecase/LoginUseCase.kt`
  - `presentation/viewmodel/LoginViewModel.kt`
  - `presentation/screen/LoginScreen.kt`

#### 2. RegisterViewModel
- **Status**: ✅ Perfect
- **Pattern**: ✅ Uses RegisterUseCase + RegisterState StateFlow + Resource<T>
- **Screen**: RegisterScreen
- **Files**:
  - `domain/usecase/RegisterUseCase.kt`
  - `presentation/viewmodel/RegisterViewModel.kt`

#### 3. HomeViewModel
- **Status**: ✅ REFACTORED (was: 3 separate flows + direct repo call)
- **Pattern**: ✅ Now uses HomeUseCase + HomeState StateFlow + Resource<T>
- **Changes**:
  - Created: `HomeUseCase.kt`
  - Modified: `ProductRepository.kt` (now returns `Resource<List<Product>>`)
  - Modified: `ProductRepositoryImpl.kt` (error wrapping)
  - Updated: `HomeState` dataclass
  - Updated: `HomeScreen.kt` (single state collection)
- **Screen**: HomeScreen (updated to use single state)

#### 4. ProfileViewModel
- **Status**: ✅ REFACTORED (was: direct UserApiService calls)
- **Pattern**: ✅ Now uses GetUserProfileUseCase + ProfileState StateFlow + Resource<T>
- **Changes**:
  - Created: `UserRepository.kt` interface
  - Created: `UserRepositoryImpl.kt`
  - Updated: `GetUserProfileUseCase.kt` (was mock data)
  - Updated: `ProfileViewModel.kt` (UseCase injection)
- **Screen**: ProfileScreen (already correct structure)

#### 5. EditProfileViewModel
- **Status**: ✅ REFACTORED (was: direct UserApiService + manual error handling)
- **Pattern**: ✅ Now uses GetUserProfileUseCase + UpdateUserProfileUseCase + EditProfileState + Resource<T>
- **Changes**:
  - Created: `UpdateUserProfileUseCase.kt`
  - Updated: `UserRepository.kt` (added updateUserProfile method)
  - Updated: `UserRepositoryImpl.kt` (implement updateUserProfile)
  - Updated: `EditProfileViewModel.kt` (both UseCases)
- **Screen**: EditProfileScreen (already correct structure)

---

### ⚠️ LOW PRIORITY - CAN BE REFACTORED LATER

#### 6. SearchViewModel
- **Status**: ⚠️ Partial (Local-only, no API dependency)
- **Current Pattern**: 
  ```kotlin
  class SearchViewModel {
      val recentSearches: StateFlow<List<String>>
      val isLoading: StateFlow<Boolean>
      val searchQuery: StateFlow<String>
  }
  ```
- **Should Refactor?**: YES, for consistency
- **Recommended Changes**:
  - Create `SearchState` dataclass
  - Combine 3 flows into 1
  - If future: Add backend search, create SearchUseCase + SearchRepository
- **Priority**: LOW (works fine as-is, refactor when adding backend search)

#### 7. SplashViewModel
- **Status**: ✅ Good (Simple timer logic)
- **Current Pattern**:
  ```kotlin
  class SplashViewModel {
      val navigateToHome: StateFlow<Boolean>
  }
  ```
- **Should Refactor?**: NO needed (too simple)
- **Priority**: N/A (minimal code, no complexity)

#### 8. IntroduceViewModel
- **Status**: ❌ Empty (Placeholder)
- **Current**:
  ```kotlin
  class IntroduceViewModel {
  }
  ```
- **Action Needed**: 
  - Define what IntroduceScreen should do
  - Then create proper ViewModel with UseCase if needed
- **Priority**: LOW (feature not defined yet)

---

## 📊 Summary Table

| ViewModel | Status | Pattern | Needs Refactor | Priority |
|-----------|--------|---------|----------------|----------|
| LoginViewModel | ✅ | ✅ Perfect | NO | - |
| RegisterViewModel | ✅ | ✅ Perfect | NO | - |
| HomeViewModel | ✅ | ✅ Refactored | NO | - |
| ProfileViewModel | ✅ | ✅ Refactored | NO | - |
| EditProfileViewModel | ✅ | ✅ Refactored | NO | - |
| SearchViewModel | ⚠️ | Partial | Optional | LOW |
| SplashViewModel | ✅ | Simple | NO | - |
| IntroduceViewModel | ❌ | Empty | YES | VERY LOW |

---

## 🔄 Optional Refactoring (For Code Consistency)

### SearchViewModel Refactor (Optional)
```kotlin
// domain/usecase/GetRecentSearchesUseCase.kt
class GetRecentSearchesUseCase @Inject constructor(
    // private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(context: Context): Resource<List<String>> {
        // Get from repository
    }
}

// presentation/viewmodel/SearchState.kt
data class SearchState(
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

// presentation/viewmodel/SearchViewModel.kt
class SearchViewModel(
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase
) : ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()
    
    fun loadRecentSearches(context: Context) {
        // ... use UseCase
    }
}
```

### When to Refactor SearchViewModel?
- ✅ When adding backend API search
- ✅ When SearchViewModel becomes complex
- ⏸️ For now: Can stay as-is (local only)

---

## ✨ Next Steps

### Phase 1: ✅ COMPLETED
- [x] LoginViewModel (reference)
- [x] RegisterViewModel
- [x] HomeViewModel (refactored)
- [x] ProfileViewModel (refactored)
- [x] EditProfileViewModel (refactored)

### Phase 2: OPTIONAL (whenever needed)
- [ ] SearchViewModel (if backend search added)
- [ ] IntroduceViewModel (once feature is defined)

### Phase 3: FUTURE FEATURES
- [ ] New screens with proper UseCase pattern
- [ ] Apply same pattern to any new features

---

## 📝 Notes

1. **All critical ViewModels are now refactored** ✅
2. **SearchViewModel can stay as-is** (local-only, simple)
3. **SplashViewModel is fine** (minimal timer logic)
4. **IntroduceViewModel is empty** (feature not defined)

### For Any New Feature:
```
1. Create Data Model (domain/model/)
2. Create Repository Interface (domain/repository/)
3. Create RepositoryImpl (data/repository/)
4. Create UseCase (domain/usecase/)
5. Create State dataclass (presentation/viewmodel/)
6. Create ViewModel (presentation/viewmodel/)
7. Create Screen (presentation/screen/)
8. Add DI binding (di/RepositoryModule.kt)
```

---

## 🎯 Testing Checklist

After all refactoring, test:

- [ ] Home screen loads products correctly
- [ ] Home screen shows loading state
- [ ] Home screen shows error state with retry button
- [ ] Profile screen loads user info when authenticated
- [ ] Profile screen shows not-authenticated prompt
- [ ] Edit profile loads current data
- [ ] Edit profile saves changes correctly
- [ ] Logout clears token and shows login prompt
- [ ] Error messages display correctly (401, 403, network errors)
- [ ] Retry buttons work

---

## 🚀 Conclusion

✅ **Codebase is now standardized with clean architecture pattern!**

All core ViewModels follow:
- ✅ Single State dataclass (not multiple flows)
- ✅ Resource<T> wrapping for all API responses
- ✅ UseCase for business logic
- ✅ Repository abstraction
- ✅ Proper error handling

Future features should follow the same pattern! 🎉


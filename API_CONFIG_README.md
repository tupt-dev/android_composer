# Android Retrofit API Client - Environment Configuration

## 🔐 Cấu hình Environment Bảo mật

Dự án này sử dụng cấu hình environment riêng biệt cho Debug và Release để đảm bảo tính bảo mật.

### 📁 Cấu trúc Files

```
network/
├── ApiClient.kt       # Retrofit client chính
├── ApiService.kt      # Interface định nghĩa API endpoints
├── ApiConfig.kt       # Cấu hình environment từ BuildConfig
├── NetworkResult.kt   # Wrapper cho API responses
└── ApiUsageExample.kt # Ví dụ sử dụng
```

### 🔧 Cấu hình Build Types

#### Debug Mode
- **Base URL**: `https://api-dev.example.com/`
- **API Key**: `dev_api_key_12345`
- **Logging**: Enabled (BODY level)
- **Minify**: Disabled

#### Release Mode
- **Base URL**: `https://api.example.com/`
- **API Key**: `prod_api_key_67890`
- **Logging**: Disabled
- **Minify**: Enabled

### 🚀 Cách sử dụng

#### 1. Cập nhật API URLs và Keys
Thay đổi trong `app/build.gradle.kts`:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BASE_URL", "\"https://your-dev-api.com/\"")
        buildConfigField("String", "API_KEY", "\"your_dev_key\"")
    }
    release {
        buildConfigField("String", "BASE_URL", "\"https://your-prod-api.com/\"")
        buildConfigField("String", "API_KEY", "\"your_prod_key\"")
    }
}
```

#### 2. Sử dụng trong ViewModel

```kotlin
class MyViewModel : ViewModel() {
    private val repository = ProductRepository()
    
    fun loadProducts() {
        viewModelScope.launch {
            repository.getAllProductsFromApi().collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        // Hiển thị loading
                    }
                    is NetworkResult.Success -> {
                        // Xử lý dữ liệu thành công
                        val products = result.data
                    }
                    is NetworkResult.Error -> {
                        // Xử lý lỗi
                        val errorMessage = result.message
                    }
                }
            }
        }
    }
}
```

#### 3. Kiểm tra Environment Info

```kotlin
// Lấy thông tin environment hiện tại
val envInfo = ApiClient.getEnvironmentInfo()
Log.d("API", envInfo)
```

### 🛡️ Bảo mật

#### Các file được bảo vệ (không commit vào Git):
- `local.env` - Chứa API keys
- `*.env` - Tất cả environment files
- `api_keys.properties` - File properties chứa keys
- `*.jks`, `*.keystore` - Keystore files

#### Khuyến nghị bảo mật:
1. **Không hard-code API keys** trong source code
2. **Sử dụng BuildConfig** để inject values
3. **Khác biệt giữa Dev và Prod** environments
4. **Disable logging** trong production
5. **Sử dụng ProGuard/R8** để obfuscate code

### 🔍 Debug Features

- **HTTP Logging**: Chỉ bật trong debug mode
- **Request/Response Logging**: Full body logging
- **Network Interceptor**: Tự động thêm headers
- **Timeout Configuration**: Có thể điều chỉnh cho từng environment

### 🌐 API Endpoints có sẵn

```kotlin
// GET tất cả products
suspend fun getProducts(): Response<List<Product>>

// GET product theo ID
suspend fun getProductById(id: Int): Response<Product>

// POST tạo product mới
suspend fun createProduct(product: Product): Response<Product>

// PUT cập nhật product
suspend fun updateProduct(id: Int, product: Product): Response<Product>

// DELETE xóa product
suspend fun deleteProduct(id: Int): Response<Unit>

// GET tìm kiếm products
suspend fun searchProducts(query: String): Response<List<Product>>
```

### 📝 Ghi chú

- Thay đổi URLs trong `buildConfigField` theo API thực tế của bạn
- Cập nhật API keys trong file `local.env` (không commit)
- Kiểm tra network permissions trong AndroidManifest.xml
- Sử dụng NetworkResult wrapper để handle errors một cách nhất quán

### 🚨 Lưu ý quan trọng

**KHÔNG BAO GIỜ** commit các file chứa API keys hoặc thông tin nhạy cảm vào version control. Luôn sử dụng environment variables hoặc BuildConfig để inject values.

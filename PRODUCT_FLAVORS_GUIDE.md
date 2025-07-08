# Product Flavors Configuration Guide

## 🏗️ Product Flavors cho Môi trường

Dự án hiện tại sử dụng **Product Flavors** để quản lý các môi trường khác nhau một cách chuyên nghiệp và linh hoạt.

### 📁 Cấu hình hiện tại

#### 1. **Dev Flavor** (`dev`)
- **Application ID**: `com.tupt.audio_composer.dev`
- **App Name**: "Audio Composer (Dev)"
- **Version Suffix**: `-dev`
- **Base URL**: `https://api-dev.example.com/`
- **API Key**: `dev_api_key_12345`
- **Logging**: Enabled
- **Environment**: `DEVELOPMENT`

#### 2. **Staging Flavor** (`staging`)
- **Application ID**: `com.tupt.audio_composer.staging`
- **App Name**: "Audio Composer (Staging)"
- **Version Suffix**: `-staging`
- **Base URL**: `https://api-staging.example.com/`
- **API Key**: `staging_api_key_456`
- **Logging**: Enabled
- **Environment**: `STAGING`

#### 3. **Production Flavor** (`prod`)
- **Application ID**: `com.tupt.audio_composer`
- **App Name**: "Audio Composer"
- **Version Suffix**: None
- **Base URL**: `https://api.example.com/`
- **API Key**: `prod_api_key_67890`
- **Logging**: Disabled
- **Environment**: `PRODUCTION`

### 🔧 Build Variants

Với Product Flavors, bạn có thể tạo ra 6 build variants:

1. **devDebug** - Development + Debug
2. **devRelease** - Development + Release
3. **stagingDebug** - Staging + Debug
4. **stagingRelease** - Staging + Release
5. **prodDebug** - Production + Debug
6. **prodRelease** - Production + Release

### 📱 Cách Build và Run

#### Android Studio:
1. Mở **Build Variants** panel
2. Chọn variant muốn build (vd: `devDebug`)
3. Click **Run** hoặc **Debug**

#### Command Line:
```bash
# Build Dev Debug
./gradlew assembleDevDebug

# Build Staging Release
./gradlew assembleStagingRelease

# Build Production Release
./gradlew assembleProdRelease

# Install Dev Debug
./gradlew installDevDebug

# Install Staging Debug
./gradlew installStagingDebug
```

### 🔍 Lợi ích của Product Flavors

1. **Cài đặt đồng thời**: Có thể cài Dev, Staging, và Prod trên cùng một thiết bị
2. **App Name khác nhau**: Dễ phân biệt các version
3. **Package Name khác nhau**: Không conflict khi cài đặt
4. **Cấu hình riêng biệt**: Mỗi môi trường có API riêng
5. **Resources riêng**: Có thể có icon, theme khác nhau

### 🛠️ Sử dụng trong Code

```kotlin
// Kiểm tra môi trường hiện tại
when {
    ApiConfig.isDevelopment -> {
        // Logic cho Development
    }
    ApiConfig.isStaging -> {
        // Logic cho Staging
    }
    ApiConfig.isProduction -> {
        // Logic cho Production
    }
}

// Lấy thông tin môi trường
val envInfo = ApiClient.getEnvironmentInfo()
Log.d("Environment", envInfo)
```

### 🎨 Tùy chỉnh thêm cho Flavors

Bạn có thể thêm các tùy chỉnh khác trong Product Flavors:

```kotlin
productFlavors {
    create("dev") {
        // ...existing config...
        
        // Custom icon cho Dev
        manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_dev"
        
        // Custom theme
        resValue("color", "primary_color", "#FF9800")
        
        // Custom strings
        resValue("string", "server_environment", "Development Server")
        
        // Custom boolean flags
        buildConfigField("boolean", "ENABLE_CRASH_REPORTING", "false")
        buildConfigField("boolean", "ENABLE_ANALYTICS", "false")
    }
    
    create("staging") {
        // ...existing config...
        manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_staging"
        resValue("color", "primary_color", "#2196F3")
        buildConfigField("boolean", "ENABLE_CRASH_REPORTING", "true")
        buildConfigField("boolean", "ENABLE_ANALYTICS", "true")
    }
    
    create("prod") {
        // ...existing config...
        manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher"
        resValue("color", "primary_color", "#4CAF50")
        buildConfigField("boolean", "ENABLE_CRASH_REPORTING", "true")
        buildConfigField("boolean", "ENABLE_ANALYTICS", "true")
    }
}
```

### 🔒 Bảo mật với Flavors

1. **Separate API Keys**: Mỗi môi trường có key riêng
2. **Different Endpoints**: URLs khác nhau cho từng môi trường
3. **Logging Control**: Tự động tắt logging trong production
4. **Obfuscation**: Chỉ áp dụng cho production builds

### 📋 Checklist khi Deploy

#### Development:
- [ ] API endpoint đúng
- [ ] Logging enabled
- [ ] Debug symbols included
- [ ] Test API keys

#### Staging:
- [ ] Production-like data
- [ ] Performance testing
- [ ] Integration testing
- [ ] User acceptance testing

#### Production:
- [ ] Production API keys
- [ ] Logging disabled
- [ ] Code obfuscation enabled
- [ ] Signed APK/AAB
- [ ] Performance optimized

### 🚀 Automation với Flavors

Bạn có thể tự động hóa deployment với GitHub Actions:

```yaml
# .github/workflows/deploy.yml
name: Deploy
on:
  push:
    branches: [main]

jobs:
  deploy-dev:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build Dev
        run: ./gradlew assembleDevRelease
      
  deploy-staging:
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/staging'
    steps:
      - uses: actions/checkout@v2
      - name: Build Staging
        run: ./gradlew assembleStagingRelease
```

Product Flavors giúp quản lý môi trường một cách chuyên nghiệp và có thể scale tốt khi dự án phát triển!

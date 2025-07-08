import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun loadProperties(filename: String): Properties? {
    val keystorePropertiesFile = rootProject.file(filename)
    val keystoreProperties = Properties()
    if (keystorePropertiesFile.exists()) {
        FileInputStream(keystorePropertiesFile).use { stream ->
            keystoreProperties.load(stream)
        }
        return keystoreProperties
    }
    return null
}

val keystoreProperties = loadProperties("secrets.properties")
android {
    namespace = "com.tupt.audio_composer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tupt.audio_composer"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://api-dev.example.com/\"")
            buildConfigField("String", "API_KEY", "\"dev_api_key_12345\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
            buildConfigField("String", "COINMARKET_BASE_URL", "https://sandbox-api.coinmarketcap.com/v1/")
            buildConfigField("String", "API_KEY", "\"${keystoreProperties?.getProperty("api_key") ?: ""}\"")
            resValue("string", "app_name", "Audio Composer (Dev)")
        }

        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "BASE_URL", "\"https://api-staging.example.com/\"")
            buildConfigField("String", "API_KEY", "\"staging_api_key_456\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String", "ENVIRONMENT", "\"STAGING\"")
            buildConfigField("String", "COINMARKET_BASE_URL", "https://sandbox-api.coinmarketcap.com/v1/")
            buildConfigField("String", "API_KEY", "\"${keystoreProperties?.getProperty("api_key") ?: ""}\"")
            resValue("string", "app_name", "Audio Composer (Staging)")
        }

        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("String", "API_KEY", "\"prod_api_key_67890\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
            buildConfigField("String", "COINMARKET_BASE_URL", "https://sandbox-api.coinmarketcap.com/v1/")
            buildConfigField("String", "API_KEY", "\"${keystoreProperties?.getProperty("api_key") ?: ""}\"")
            resValue("string", "app_name", "Audio Composer")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // DataStore for Settings
    implementation(libs.androidx.datastore.preferences)

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.gson)

    // Add experimental Material 3 for pull-to-refresh
    implementation(libs.androidx.material3.android)
    implementation(libs.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
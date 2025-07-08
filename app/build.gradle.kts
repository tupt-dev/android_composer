import com.google.android.libraries.mapsplatform.secrets_gradle_plugin.SecretsPluginExtension.Companion.defaultPropertiesFile
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
            resValue("string", "app_name", "Audio Composer (Dev)")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String","ENVIRONMENT", "\"DEVELOPMENT\"")
        }

        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "Audio Composer (Staging)")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String","ENVIRONMENT", "\"STAGING\"")

        }

        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "Audio Composer")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            buildConfigField("String","ENVIRONMENT", "\"PRODUCTION\"")
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
            signingConfig = signingConfigs.getByName("debug")
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
    secrets {
        productFlavors  {
            getByName("dev") {
                defaultPropertiesFileName = "dev.properties"
            }
            getByName("staging") {
                defaultPropertiesFileName = "staging.properties"
            }
            getByName("prod") {
                defaultPropertiesFileName = "production.properties"
            }
        }
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
    implementation(libs.androidx.security.crypto)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
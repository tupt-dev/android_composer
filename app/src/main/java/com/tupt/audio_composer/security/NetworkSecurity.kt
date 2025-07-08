package com.tupt.audio_composer.security

import com.tupt.audio_composer.config.ApiConfig
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkSecurity {

    // Certificate Pinning configuration
    private fun createCertificatePinner(): CertificatePinner {
        val builder = CertificatePinner.Builder()

        // Chỉ áp dụng certificate pinning trong production
        if (ApiConfig.isProduction && ApiConfig.Security.CERTIFICATE_PINNING_ENABLED) {
            ApiConfig.Security.CERTIFICATE_PINS.forEach { (hostname, pins) ->
                pins.forEach { pin ->
                    builder.add(hostname, pin)
                }
            }
        }

        return builder.build()
    }

    // Tạo Trust Manager cho development/staging
    private fun createTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {
                // Chỉ bỏ qua kiểm tra trong development
                if (ApiConfig.isProduction) {
                    throw SecurityException("Client certificates not allowed in production")
                }
            }

            override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {
                // Chỉ bỏ qua kiểm tra trong development
                if (ApiConfig.isProduction) {
                    // Thực hiện kiểm tra certificate thông thường
                    // Implement proper certificate validation here
                }
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    }

    // Tạo SSL Context bảo mật
    private fun createSSLContext(): SSLContext {
        return if (ApiConfig.isProduction) {
            // Sử dụng SSL context mặc định cho production
            SSLContext.getDefault()
        } else {
            // Tạo SSL context custom cho development/staging
            val trustManager = createTrustManager()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            sslContext
        }
    }

    // Tạo OkHttpClient với cấu hình bảo mật
    fun createSecureOkHttpClient(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

        // Logging interceptor
        if (ApiConfig.isDebugMode()) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
                // Obfuscate sensitive data in logs
                if (ApiConfig.Security.OBFUSCATE_LOGS) {
                    redactHeader("Authorization")
                    redactHeader("X-API-Key")
                    redactHeader("Cookie")
                }
            }
            builder.addInterceptor(loggingInterceptor)
        }

        // Certificate pinning
        if (ApiConfig.Security.CERTIFICATE_PINNING_ENABLED) {
            builder.certificatePinner(createCertificatePinner())
        }

        // SSL configuration
        try {
            val sslContext = createSSLContext()
            if (!ApiConfig.isProduction) {
                builder.sslSocketFactory(sslContext.socketFactory, createTrustManager())
            }
        } catch (e: Exception) {
            // Log error but don't crash the app
            if (ApiConfig.isDebugMode()) {
                e.printStackTrace()
            }
        }

        return builder
    }

    // Validate API key format
    fun isValidApiKey(apiKey: String): Boolean {
        return apiKey.isNotBlank() &&
               apiKey.length >= 32 &&
               apiKey.matches(Regex("^[A-Za-z0-9_-]+$"))
    }

    // Obfuscate sensitive data for logging
    fun obfuscateForLogging(data: String): String {
        return if (ApiConfig.Security.OBFUSCATE_LOGS && data.length > 8) {
            "${data.take(4)}****${data.takeLast(4)}"
        } else {
            data
        }
    }

    // Security headers validation
    fun validateSecurityHeaders(headers: Map<String, String>): Boolean {
        val requiredHeaders = listOf(
            ApiConfig.Headers.X_REQUESTED_WITH,
            ApiConfig.Headers.X_API_VERSION,
            ApiConfig.Headers.X_ENVIRONMENT
        )

        return requiredHeaders.all { headerName ->
            headers.containsKey(headerName) && !headers[headerName].isNullOrBlank()
        }
    }
}

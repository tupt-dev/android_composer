package com.tupt.audio_composer.model

/**
 * Data class representing an audio product
 */
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val durationInSeconds: Int,
    val thumbnailUrl: String? = null
)

object SampleProducts {
    val products = listOf(
        Product(
            id = "1",
            name = "Summer Beat",
            description = "A light and upbeat summer track perfect for vlogs and outdoor content",
            durationInSeconds = 180,
            thumbnailUrl = null
        ),
        Product(
            id = "2",
            name = "Deep Focus",
            description = "Ambient track designed to enhance concentration and productivity",
            durationInSeconds = 360,
            thumbnailUrl = null
        ),
        Product(
            id = "3",
            name = "Cinematic Score",
            description = "Dramatic orchestral piece for storytelling and emotional impact",
            durationInSeconds = 240,
            thumbnailUrl = null
        ),
        Product(
            id = "4",
            name = "Lo-fi Groove",
            description = "Relaxing beats for studying and chilling",
            durationInSeconds = 300,
            thumbnailUrl = null
        ),
        Product(
            id = "5",
            name = "Epic Trailer",
            description = "Powerful and dramatic composition for impactful presentations",
            durationInSeconds = 120,
            thumbnailUrl = null
        )
    )

    fun getProductById(id: String): Product? {
        return products.find { it.id == id }
    }
}

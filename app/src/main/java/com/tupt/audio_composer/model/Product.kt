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
        ),
        Product(
            id = "6",
            name = "Nature Sounds",
            description = "Soothing sounds of nature for relaxation and meditation",
            durationInSeconds = 600,
            thumbnailUrl = null
        ),
        Product(
            id = "7",
            name = "Tech Vibes",
            description = "Modern electronic track for tech-related content",
            durationInSeconds = 150,
            thumbnailUrl = null
        ),
        Product(
            id = "8",
            name = "Jazz Nights",
            description = "Smooth jazz for evening relaxation and ambiance",
            durationInSeconds = 420,
            thumbnailUrl = null
        ),
        Product(
            id = "9",
            name = "Upbeat Pop",
            description = "Catchy pop tune for energetic and fun content",
            durationInSeconds = 200,
            thumbnailUrl = null
        ),
        Product(
            id = "10",
            name = "World Rhythms",
            description = "Diverse global sounds for cultural and travel content",
            durationInSeconds = 300,
            thumbnailUrl = null
        ),
        Product(
            id = "11",
            name = "Acoustic Bliss",
            description = "Warm acoustic guitar melodies for a cozy atmosphere",
            durationInSeconds = 180,
            thumbnailUrl = null
        ),
        Product(
            id = "12",
            name = "Epic Adventure",
            description = "An adventurous orchestral piece for epic storytelling",
            durationInSeconds = 240,
            thumbnailUrl = null
        ),
        Product(
            id = "13",
            name = "Chill Beats",
            description = "Relaxing beats for studying and chilling",
            durationInSeconds = 300,
            thumbnailUrl = null
        ),
    )

    fun getProductById(id: String): Product? {
        return products.find { it.id == id }
    }
}

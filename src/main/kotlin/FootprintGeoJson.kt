import kotlinx.serialization.Serializable

@Serializable
data class FootprintGeoJSON(
    val type: String,
    val crs: CRS,
    val features: List<Feature>
) {
    @Serializable
    data class CRS(
        val type: String,
        val properties: Properties
    ) {
        @Serializable
        data class Properties(
            val name: String
        )
    }

    @Serializable
    data class Feature(
        val type: String,
        val properties: Properties,
        val geometry: Geometry
    ) {
        @Serializable
        data class Properties(
            val type: String,
            val properties: Properties
        ) {
            @Serializable
            data class Properties(
                val height: Double,
                val confidence: Double
            )
        }

        @Serializable
        data class Geometry(
            val type: String,
            val coordinates: List<List<List<Double>>>
        )
    }
}
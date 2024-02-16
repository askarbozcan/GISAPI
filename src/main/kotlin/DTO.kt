import kotlinx.serialization.Serializable

object DTO {


    @Serializable
    data class JsonPoint(
        val lat: Double,
        val lng: Double
    )
    @Serializable
    data class GET(
        val topLeft: JsonPoint,
        val bottomRight: JsonPoint,
        val zoomLevel: Double,
        val amenities: Set<String>,
        val nPolys: Int? = 100
    )
}

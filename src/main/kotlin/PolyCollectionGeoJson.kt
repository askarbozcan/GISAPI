import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PolyGeoJson(
    val geometry: Geometry,
    val properties: Properties,
) {
    val type = "Feature"

    @Serializable
    data class Geometry(
        val coordinates: List<List<List<Double>>>,
    ) {
        val type = "Polygon"
    }

    // set kebab case for serialization


    @Serializable
    data class Properties(
        val missingAmenities: List<String>,
        val score: Double,
    )
}

@Serializable
data class PolyCollectionGeoJson(
    val features: List<PolyGeoJson>
) {
    val type = "FeatureCollection"

    companion object {
        fun fromListOfBuildingAmenitiesDTO(buaDtos: List<BuildingsAmenitiesDTO>, selectedAmenities: Set<String>): PolyCollectionGeoJson {
            val features = buaDtos.map { dto ->
                val score = dto.amenities.intersect(selectedAmenities).size.toDouble() / selectedAmenities.size
                val missingAmenities = selectedAmenities.minus(dto.amenities).toList()
                val coordinates = (0 until dto.buildingWay.numPoints()).map { i ->
                    val point = dto.buildingWay.getPoint(i)
                    listOf(point.x, point.y)
                }
                val geometry = PolyGeoJson.Geometry(listOf(coordinates))
                val properties = PolyGeoJson.Properties(missingAmenities, score)
                PolyGeoJson(geometry, properties)
            }
            return PolyCollectionGeoJson(features)
        }
    }
}
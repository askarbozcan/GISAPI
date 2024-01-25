import kotlinx.serialization.Serializable

object DTO {
    @Serializable
    data class PolyCollectionGeoJson(
      val features: List<PolyGeoJson>
    ) {
        val type = "FeatureCollection"

        @Serializable
        data class PolyGeoJson(
            val geometry: Geometry,
            val properties: Properties
        ) {
            val type = "Feature"

            @Serializable
            data class Geometry(
                val coordinates: List<List<List<Double>>>,
            ) {
                val type = "Polygon"
            }

            @Serializable
            data class Properties(
                val score: Double,
                val height: Double = -1.0,
            )
        }

        companion object {
            fun fromListOfModelPolygons(polys: List<FootprintsGeo.GeoPolygon>): PolyCollectionGeoJson {
                val features = polys.map { poly ->
                    val coordinates = poly.polygon.points.map { point ->
                        listOf(point.longitude, point.latitude)
                    }
                    val geometry = PolyGeoJson.Geometry(listOf(coordinates))
                    val properties = PolyGeoJson.Properties(poly.score, poly.height)
                    PolyGeoJson(geometry, properties)
                }
                return PolyCollectionGeoJson(features)
            }
        }
    }


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
        val amenities: List<String>,
        val nPolys: Int? = 100
    )
}

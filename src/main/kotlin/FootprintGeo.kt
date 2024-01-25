data class FootprintsGeo(
    val geoPolygons: List<GeoPolygon>
) {
    data class GeoPolygon(
        val height: Double,
        val polygon: Polygon,
        val score: Double = 0.0
    ) {
        data class Polygon(
            val points: List<Point>
        ) {
            data class Point(
                val latitude: Double,
                val longitude: Double
            )
        }
    }

    companion object {
        fun fromGeoJson(geoJson: FootprintGeoJSON): FootprintsGeo {
            val geoPolygons = geoJson.features.map { feature ->
                val height = feature.properties.properties.height
                   val polygons = feature.geometry.coordinates.map { polygon ->
                    val points = polygon.map { point ->
                        val longitude = point[0]
                        val latitude = point[1]
                        GeoPolygon.Polygon.Point(latitude, longitude)
                    }
                    GeoPolygon.Polygon(points)
                   }
                GeoPolygon(height, polygons.first())
            }
            return FootprintsGeo(geoPolygons)
        }
    }
}
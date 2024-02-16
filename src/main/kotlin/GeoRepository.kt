import net.postgis.jdbc.PGgeometry
import net.postgis.jdbc.geometry.Geometry
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import javax.sql.DataSource

class GeoRepository(private val dataSource: DataSource) {
    private val jdbi: Jdbi = Jdbi.create(dataSource)

    private val amenitiesQuery = """
        select 
        	bua.building_id, bua.amenities, bua.way
        from 
        	buildings_uniq_amenities bua 
        where bua.way && st_makeenvelope(:minLong, :minLat, :maxLong, :maxLat, 4326);
    """.trimIndent()

    fun getPolygonsWithPercScore(minLat: Double, minLong: Double, maxLat: Double, maxLong: Double, selectedAmenities: Set<String>): PolyCollectionGeoJson {
        val buildingsAmenitiesDTO = jdbi.withHandle<List<BuildingsAmenitiesDTO>, Exception> { handle ->
            handle.createQuery(amenitiesQuery)
                .bind("minLong", minLong)
                .bind("minLat", minLat)
                .bind("maxLong", maxLong)
                .bind("maxLat", maxLat)
                .map { rs,ctx ->
                    BuildingsAmenitiesDTO(
                        buildingId = rs.getBigDecimal("building_id"),
                        amenities = (rs.getArray("amenities").array as Array<*>).toSet() as Set<String>,
                        buildingWay = (rs.getObject("way") as PGgeometry).geometry
                    )
                }
                .toList()
        }

        return PolyCollectionGeoJson.fromListOfBuildingAmenitiesDTO(buildingsAmenitiesDTO, selectedAmenities)
    }

    private val listAmenities = """
        select distinct kp.amenity_tag from kn_pois kp;
    """.trimIndent()

    fun getAmenitiesList(): Set<String> {
        return jdbi.withHandle<Set<String>, Exception>  { handle ->
            handle.createQuery(listAmenities)
                .map { rs, _ -> rs.getString("amenity_tag") }
                .toSet()
        }
    }

}
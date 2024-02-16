import net.postgis.jdbc.geometry.Geometry
import java.math.BigDecimal

data class BuildingsAmenitiesDTO (
    val buildingId: BigDecimal,
    val buildingWay: Geometry,
    val amenities: Set<String>
)

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.javalin.Javalin
import io.javalin.plugin.bundled.CorsPluginConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class Main : CliktCommand() {
    val jsonsPath by option(help = "Path to JSON files").required()
    override fun run() {
        val footprintsFile = File(jsonsPath)
            .walk()
            .filter { it.isFile && it.extension == "geojson" }
            .first()
        val footprintsJson = Json.decodeFromString<FootprintGeoJSON>(footprintsFile.readText())

        val footprintsGeo = FootprintsGeo.fromGeoJson(footprintsJson)
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        val app = Javalin.create{config ->
            config.plugins.enableCors{ cors ->
                cors.add(CorsPluginConfig::anyHost)
            }
        }.post("/polygons") { ctx ->
                println(ctx.body())
                val req = json.decodeFromString<DTO.GET>(ctx.body())
                val polys = footprintsGeo.geoPolygons
                    .filter { poly ->
                        val points = poly.polygon.points
                        val topLeft = req.topLeft
                        val bottomRight = req.bottomRight
                        val inLat = points.all { it.latitude <= topLeft.lat && it.latitude >= bottomRight.lat }
                        val inLng = points.all { it.longitude >= topLeft.lng && it.longitude <= bottomRight.lng }
                        inLat && inLng
                    }
                    .take(req.nPolys ?: 1000)

                val polyCollection = DTO.PolyCollectionGeoJson.fromListOfModelPolygons(polys)
                ctx.status(200)
                    .result(json.encodeToString(polyCollection))
                    .contentType("application/json")
            }
            .start(7123)
    }
}
fun main(args: Array<String>) = Main().main(args)

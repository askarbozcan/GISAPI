import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.javalin.Javalin
import io.javalin.plugin.bundled.CorsPluginConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.postgresql.ds.PGSimpleDataSource
import java.io.File

class Main : CliktCommand() {
    val dbUser by option(help = "Database user", envvar = "DB_USER").required()
    val dbPass by option(help = "Database password", envvar = "DB_PASS").required()
    val dbHost by option(help = "Database host", envvar = "DB_HOST").required()
    val dbName by option(help = "Database name", envvar = "DB_NAME").required()
    val dbPort by option(help = "Database port", envvar = "DB_PORT").default("5432")
    val dbSslMode by option(help = "Database SSL mode", envvar = "DB_SSL_MODE").default("require")

    val jsonsPath by option(help = "Path to JSON files (deprecated)")
    override fun run() {
        val ds = PGSimpleDataSource()
        ds.user = dbUser
        ds.password = dbPass
        ds.serverNames = arrayOf(dbHost)
        ds.databaseName = dbName
        ds.portNumbers = intArrayOf(dbPort.toInt())
        ds.sslmode = dbSslMode

        val geoRepo = GeoRepository(ds)

        val json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        val app = Javalin.create{config ->
            config.plugins.enableCors { cors ->
                cors.add {it.anyHost()}
            }
            }.post("/polygons") { ctx ->
                println(ctx.body())
                val req = json.decodeFromString<DTO.GET>(ctx.body())
                val polyCollection = geoRepo.getPolygonsWithPercScore(
                    req.topLeft.lat,
                    req.topLeft.lng,
                    req.bottomRight.lat,
                    req.bottomRight.lng,
                    req.amenities
                )

                ctx.status(200)
                    .result(json.encodeToString(polyCollection))
                    .contentType("application/json")
            }.get("/amenitiesList") { ctx ->
                val amenitiesList = geoRepo.getAmenitiesList()
                ctx.status(200)
                    .result(json.encodeToString(amenitiesList))
                    .contentType("application/json")
            }
            .start(7123)
    }
}
fun main(args: Array<String>) = Main().main(args)

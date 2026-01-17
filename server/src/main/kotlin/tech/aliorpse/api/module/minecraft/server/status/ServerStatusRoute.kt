package tech.aliorpse.api.module.minecraft.server.status

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import tech.aliorpse.api.shared.model.error
import tech.aliorpse.api.shared.model.success
import tech.aliorpse.mcutils.api.MCServer
import tech.aliorpse.mcutils.api.getStatus
import tech.aliorpse.mcutils.util.hostPortOf

private const val DEFAULT_PORT = 25565

fun Route.minecraftServerStatusRoute() {
    get("/minecraft/server/status") {
        val address = call.parameters["address"] ?: return@get call.error(message = "Missing parameter: target")
        val (host, port) = hostPortOf(address)

        call.success(
            MCServer.getStatus(
                host ?: return@get call.error(code = 2, message = "Invalid address: $address"),
                port ?: DEFAULT_PORT
            )
        )
    }
}

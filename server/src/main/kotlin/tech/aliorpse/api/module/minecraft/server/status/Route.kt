package tech.aliorpse.api.module.minecraft.server.status

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import tech.aliorpse.api.module.minecraft.server.status.model.DescriptionWithHtml
import tech.aliorpse.api.module.minecraft.server.status.model.ServerStatusResponse
import tech.aliorpse.api.shared.model.error
import tech.aliorpse.api.shared.model.success
import tech.aliorpse.mcutils.api.MCServer
import tech.aliorpse.mcutils.api.getQueryFull
import tech.aliorpse.mcutils.api.getStatus
import tech.aliorpse.mcutils.util.hostPortOf
import tech.aliorpse.mcutils.util.toHtml

private const val DEFAULT_PORT = 25565

fun Route.minecraftServerStatusRoute() {
    get("/minecraft/server/status") {
        val address = call.parameters["address"]
            ?: return@get call.error(code = 1, message = "Missing parameter: address")

        val (host, port) = hostPortOf(address)
        if (host == null) return@get call.error(code = 2, message = "Invalid address: $address")

        val statusPort = port ?: DEFAULT_PORT
        val queryPort = call.parameters["query"]?.toIntOrNull()

        val response = runCatching {
            val status = MCServer.getStatus(host, statusPort, timeout = 5000)
            val query = queryPort?.let { MCServer.getQueryFull(status.srvRecord ?: host, it, timeout = 5000) }

            ServerStatusResponse(
                DescriptionWithHtml(status.description, status.description.toHtml()),
                status.players,
                status.version,
                status.ping,
                status.secureChatEnforced,
                status.favicon,
                status.srvRecord,
                query?.map,
                query?.plugins
            )
        }.getOrElse {
            return@get call.error(code = -1, message = it.message ?: "${it::class.simpleName}")
        }

        call.success(response)
    }
}

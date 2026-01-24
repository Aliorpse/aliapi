package tech.aliorpse.api.module.minecraft.server.status

import io.ktor.http.HttpStatusCode.Companion.GatewayTimeout
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.resources.get
import io.ktor.server.routing.Route
import tech.aliorpse.api.module.minecraft.Minecraft
import tech.aliorpse.api.module.minecraft.server.status.model.DescriptionWithHtml
import tech.aliorpse.api.module.minecraft.server.status.model.ServerStatusResponse
import tech.aliorpse.api.shared.model.ModuleController
import tech.aliorpse.api.shared.model.failure
import tech.aliorpse.api.shared.model.success
import tech.aliorpse.mcutils.api.MCServer
import tech.aliorpse.mcutils.api.getQueryFull
import tech.aliorpse.mcutils.api.getStatus
import tech.aliorpse.mcutils.util.hostPortOf
import tech.aliorpse.mcutils.util.toHtml
import java.net.ConnectException

private const val DEFAULT_PORT = 25565

class MinecraftServerStatusController : ModuleController {
    override fun Route.registerRoutes() {
        get<Minecraft.Server.Status> { params ->
            val (host, port) = hostPortOf(params.address)
            if (host == null) throw RequestValidationException(params.address, listOf("Invalid address format"))

            val statusPort = port ?: DEFAULT_PORT

            val status = MCServer.getStatus(host, statusPort, timeout = 5000)
            val query = params.query?.let {
                MCServer.getQueryFull(
                    hostPortOf(status.srvRecord).host ?: host,
                    it,
                    timeout = 5000
                )
            }

            call.success(
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
            )
        }
    }

    override fun StatusPagesConfig.registerStatusPages() {
        exception<ConnectException> { call, cause ->
            call.failure(cause.message ?: cause::class.simpleName!!, GatewayTimeout)
        }
    }
}

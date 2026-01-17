package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import tech.aliorpse.api.module.base.baseRoute
import tech.aliorpse.api.module.minecraft.server.status.minecraftServerStatusRoute
import tech.aliorpse.api.module.steam.galgame.steamGalgameRoute

fun Application.configureRouting() {
    routing {
        baseRoute()
        minecraftServerStatusRoute()
        steamGalgameRoute()
    }
}

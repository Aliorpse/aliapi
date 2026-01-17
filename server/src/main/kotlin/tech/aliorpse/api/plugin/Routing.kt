package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import tech.aliorpse.api.module.base.baseRoute
import tech.aliorpse.api.module.minecraft.server.status.minecraftServerStatusRoute

fun Application.configureRouting() {
    routing {
        baseRoute()
        minecraftServerStatusRoute()
    }
}

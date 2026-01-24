package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import tech.aliorpse.api.controllers

fun Application.configureRouting() {
    routing {
        controllers.forEach { with(it) { registerRoutes() } }
    }
}

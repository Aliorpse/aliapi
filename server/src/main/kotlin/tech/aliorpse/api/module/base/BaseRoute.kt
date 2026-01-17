package tech.aliorpse.api.module.base

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import tech.aliorpse.api.shared.model.success

fun Route.baseRoute() {
    get("/") {
        call.success(message = "Ciallo ~")
    }
}

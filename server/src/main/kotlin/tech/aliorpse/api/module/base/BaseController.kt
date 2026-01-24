package tech.aliorpse.api.module.base

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import tech.aliorpse.api.shared.model.ModuleController
import tech.aliorpse.api.shared.model.success

class BaseController : ModuleController {
    override fun Route.registerRoutes() {
        get("/") {
            call.success("Ciallo ~ Please refer to https://github.com/Aliorpse/aliapi")
        }
    }
}

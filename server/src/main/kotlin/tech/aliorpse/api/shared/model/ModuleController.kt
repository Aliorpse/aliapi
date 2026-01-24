package tech.aliorpse.api.shared.model

import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.routing.Route
import kotlinx.coroutines.Job

interface ModuleController {
    fun Route.registerRoutes() {}

    fun StatusPagesConfig.registerStatusPages() {}

    fun RequestValidationConfig.registerValidation() {}

    val jobList: List<Job> get() = emptyList()
}

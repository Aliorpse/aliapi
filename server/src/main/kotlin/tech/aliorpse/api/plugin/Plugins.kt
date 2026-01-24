package tech.aliorpse.api.plugin

import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.GatewayTimeout
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import tech.aliorpse.api.controllers
import tech.aliorpse.api.modules
import tech.aliorpse.api.shared.model.failure
import kotlin.time.Duration.Companion.seconds

fun Application.configurePlugins() {
    // It can't be called in implicit receivers
    val developmentMode = developmentMode

    install(Koin) {
        slf4jLogger()
        modules(modules)
    }

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(CallLogging) {
        level = if (developmentMode) Level.INFO else Level.WARN
    }

    install(Resources)

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (developmentMode) {
                call.respond(HttpStatusCode.InternalServerError, cause.stackTraceToString())
            } else {
                call.failure("Internal Server Error")
            }
        }

        exception<RequestValidationException> { call, cause ->
            call.failure(cause.reasons.joinToString(), BadRequest)
        }

        exception<TimeoutCancellationException> { call, cause ->
            call.failure(cause.message ?: "Timed out", GatewayTimeout)
        }

        controllers.forEach { with(it) { registerStatusPages() } }
    }

    install(RateLimit) {
        global {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
        }
    }

    install(Compression)

    install(RequestValidation) {
        controllers.forEach { with(it) { registerValidation() } }
    }
}

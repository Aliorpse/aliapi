package tech.aliorpse.api.shared.model

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: Short,
    val message: String?,
    val data: T?
)

suspend inline fun <reified T> ApplicationCall.success(data: T) =
    respond(ApiResponse(0, null, data))

suspend inline fun ApplicationCall.success(message: String) =
    respond(ApiResponse(0, message, null))

suspend fun ApplicationCall.error(code: Short = 1, message: String) =
    respond(ApiResponse(code, message, null))

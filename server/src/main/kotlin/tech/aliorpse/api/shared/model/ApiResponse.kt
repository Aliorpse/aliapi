package tech.aliorpse.api.shared.model

import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val message: String?,
    val data: T?
)

suspend inline fun <reified T> ApplicationCall.success(data: T) =
    respond(HttpStatusCode.OK, ApiResponse("success", data))

suspend fun ApplicationCall.failure(message: String, code: HttpStatusCode = InternalServerError) =
    respond(code, ApiResponse(message, null))

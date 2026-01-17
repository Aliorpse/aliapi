package tech.aliorpse.api.module.steam.galgame.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIMEOUT = 5000L

@Suppress("MaxLineLength")
val httpClient = HttpClient(CIO) {
    install(HttpRequestRetry) {
        maxRetries = 3
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    engine {
        requestTimeout = TIMEOUT
        endpoint {
            keepAliveTime = TIMEOUT
            connectTimeout = TIMEOUT
            connectAttempts = 2
        }
    }

    defaultRequest {
        header(HttpHeaders.AcceptLanguage, "zh-CN,zh;q=0.9,en;q=0.8")
        header(
            HttpHeaders.UserAgent,
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
    }
}

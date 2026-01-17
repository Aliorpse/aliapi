package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import tech.aliorpse.api.shared.database.RedisClient

fun Application.configureDatabase() {
    val host = environment.config.property("redis.host").getString()
    val port = environment.config.property("redis.port").getString().toInt()
    val password = environment.config.property("redis.password").getString()

    RedisClient.init(host, port, password)

    monitor.subscribe(ApplicationStopped) {
        RedisClient.close()
    }
}

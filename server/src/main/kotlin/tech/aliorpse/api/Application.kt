package tech.aliorpse.api

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import tech.aliorpse.api.plugin.configureDatabase
import tech.aliorpse.api.plugin.configureRouting
import tech.aliorpse.api.plugin.configureScheduling
import tech.aliorpse.api.plugin.configureSerialization

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureDatabase()
    configureSerialization()
    configureScheduling()
}

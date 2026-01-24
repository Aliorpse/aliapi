package tech.aliorpse.api

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin
import tech.aliorpse.api.module.base.BaseController
import tech.aliorpse.api.module.minecraft.server.status.MinecraftServerStatusController
import tech.aliorpse.api.module.steam.galgame.SteamGalgameController
import tech.aliorpse.api.plugin.configureDatabase
import tech.aliorpse.api.plugin.configurePlugins
import tech.aliorpse.api.plugin.configureRouting
import tech.aliorpse.api.plugin.configureScheduling
import tech.aliorpse.api.shared.model.ModuleController

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configurePlugins()
    configureDatabase()
    configureScheduling()
    configureRouting()
}

val controllers by lazy { getKoin().getAll<ModuleController>() }

val modules = module {
    single { BaseController() } bind ModuleController::class
    single { SteamGalgameController() } bind ModuleController::class
    single { MinecraftServerStatusController() } bind ModuleController::class
}

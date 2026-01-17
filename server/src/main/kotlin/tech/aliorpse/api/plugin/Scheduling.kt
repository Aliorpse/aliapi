package tech.aliorpse.api.plugin

import io.ktor.server.application.Application
import tech.aliorpse.api.module.steam.galgame.SteamGalgameService
import tech.aliorpse.api.shared.util.TaskScheduler

fun Application.configureScheduling() {
    TaskScheduler.launch(
        hour = 0,
        minute = 0,
        name = "UpdateSteamGalgame"
    ) {
        SteamGalgameService.updateAllGameData()
    }
}

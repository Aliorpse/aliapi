package tech.aliorpse.api.module.steam.galgame

import eu.vendeli.rethis.command.serde.hVals
import io.ktor.server.resources.get
import io.ktor.server.routing.Route
import tech.aliorpse.api.module.steam.Steam
import tech.aliorpse.api.module.steam.galgame.model.SteamGalgame
import tech.aliorpse.api.shared.database.RedisClient.redisClient
import tech.aliorpse.api.shared.model.ModuleController
import tech.aliorpse.api.shared.model.success
import tech.aliorpse.api.shared.util.TaskScheduler

class SteamGalgameController : ModuleController {
    override fun Route.registerRoutes() {
        get<Steam.Galgame> {
            call.success(redisClient.hVals<SteamGalgame>("steamGalgame:gameData"))
        }
    }

    override val jobList = listOf(
        TaskScheduler.new(
            hour = 0,
            minute = 0,
            name = "UpdateSteamGalgame"
        ) {
            SteamGalgameService.updateAllGameData()
        }
    )
}

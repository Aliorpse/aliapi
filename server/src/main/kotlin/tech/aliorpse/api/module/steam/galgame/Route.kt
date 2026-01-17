package tech.aliorpse.api.module.steam.galgame

import eu.vendeli.rethis.command.serde.hVals
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import tech.aliorpse.api.module.steam.galgame.model.SteamGalgame
import tech.aliorpse.api.shared.database.RedisClient.redisClient
import tech.aliorpse.api.shared.model.success

fun Route.steamGalgameRoute() {
    get("/steam/galgame") {
        call.success(redisClient.hVals<SteamGalgame>("steamGalgame:gameData"))
    }
}

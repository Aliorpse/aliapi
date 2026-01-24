package tech.aliorpse.api.module.steam

import io.ktor.resources.Resource

@Resource("/steam")
class Steam {
    @Resource("galgame")
    class Galgame(val parent: Steam = Steam())
}

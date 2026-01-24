package tech.aliorpse.api.module.minecraft

import io.ktor.resources.Resource

@Resource("/minecraft")
class Minecraft {
    @Resource("server")
    class Server(val parent: Minecraft = Minecraft()) {
        @Resource("status")
        class Status(
            val address: String,
            val query: Int? = null,
            val parent: Server = Server()
        )
    }
}

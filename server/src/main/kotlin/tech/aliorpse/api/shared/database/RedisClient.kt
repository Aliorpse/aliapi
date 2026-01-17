package tech.aliorpse.api.shared.database

import eu.vendeli.rethis.ReThis
import eu.vendeli.rethis.types.common.Address

object RedisClient {
    @Suppress("MemberNameEqualsClassName")
    lateinit var redisClient: ReThis
        private set

    fun init(host: String, port: Int, password: String) {
        redisClient = ReThis.standalone(Address(host, port)) {
            auth(password.toCharArray())
        }
    }

    fun close() = redisClient.close()
}

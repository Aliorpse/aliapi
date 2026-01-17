package tech.aliorpse.api.module.steam.galgame.model

import kotlinx.serialization.Serializable

@Serializable
data class SteamGalgame(
    val link: String = "",
    val appId: String,
    val name: String,
    val bg: String
)

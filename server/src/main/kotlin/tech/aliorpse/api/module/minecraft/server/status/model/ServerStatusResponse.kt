package tech.aliorpse.api.module.minecraft.server.status.model

import kotlinx.serialization.Serializable
import tech.aliorpse.mcutils.entity.Players
import tech.aliorpse.mcutils.entity.TextComponent
import tech.aliorpse.mcutils.entity.Version

@Serializable
data class ServerStatusResponse(
    val description: DescriptionWithHtml,
    val players: Players,
    val version: Version,
    val ping: Long,
    val secureChatEnforced: Boolean,
    val favicon: String?,
    val srvRecord: String?,
    val map: String?,
    val plugins: Set<String>?
)

@Serializable
data class DescriptionWithHtml(
    val component: TextComponent,
    val html: String
)

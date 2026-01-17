package tech.aliorpse.api.module.steam.galgame

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import eu.vendeli.rethis.command.hash.hExists
import eu.vendeli.rethis.command.hash.hSet
import eu.vendeli.rethis.shared.request.common.FieldValue
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json
import tech.aliorpse.api.module.steam.galgame.model.SteamGalgame
import tech.aliorpse.api.module.steam.galgame.util.httpClient
import tech.aliorpse.api.shared.database.RedisClient.redisClient
import tech.aliorpse.api.shared.util.logger

private const val CONCURRENT_LIMIT = 7
private const val REDIS_KEY = "steamGalgame:gameData"
private const val BASE_URL = "https://steamgalgame.com"

object SteamGalgameService {
    private val logger = logger()

    private val steamAppIdRegex = Regex("""app/(\d+)""")

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    suspend fun updateAllGameData() {
        logger.info("Start updating game data pipeline...")

        pageFlow()
            .onEach { logger.info("Processing page containing ${it.size} links") }
            .flatMapConcat { links -> links.asFlow() } // Flatten a list of links to a stream of single links
            .filterNot { redisClient.hExists(REDIS_KEY, it) }
            .flatMapMerge(concurrency = CONCURRENT_LIMIT) { link ->
                flow {
                    fetchGameDetails(link)?.let { emit(it) }
                }
            }
            .collect { saveGameToRedis(it) }

        logger.info("Update pipeline finished.")
    }

    private fun pageFlow(): Flow<Set<String>> = flow {
        var pageIndex = 1
        while (true) {
            val html = runCatching {
                httpClient.get("$BASE_URL/page/$pageIndex/").bodyAsText()
            }.getOrElse {
                logger.error("Failed to fetch page list $pageIndex", it)
                break
            }

            val links = extractLinksFromHtml(html)
            if (links.isEmpty()) break

            emit(links)
            pageIndex++
        }
    }

    private fun extractLinksFromHtml(html: String): Set<String> =
        Ksoup.parse(html)
            .select("#app-main .mdui-card-primary-title > a[href]")
            .map { it.attr("href") }
            .toSet()

    private suspend fun fetchGameDetails(link: String): SteamGalgame? {
        return runCatching {
            val html = httpClient.get(link).bodyAsText()
            Ksoup.parse(html).parseGameDetails(link)
        }.onFailure {
            logger.error("Failed to parse game details for $link", it)
        }.getOrNull()
    }

    private suspend fun saveGameToRedis(game: SteamGalgame) {
        runCatching {
            redisClient.hSet(REDIS_KEY, FieldValue(game.link, json.encodeToString(game)))
            logger.info("Saved/Updated: ${game.name}")
        }.onFailure {
            logger.error("Redis write failed for ${game.name}", it)
        }
    }

    private fun Document.parseGameDetails(link: String): SteamGalgame {
        val tableRows = select("tr")

        // Find the Steam Store link
        val storeLinkElement = tableRows.firstNotNullOfOrNull { row ->
            val cells = row.select("td")
            // Check headers, handles standard layout
            if (cells.size >= 2 && cells[0].text().contains("商店链接")) {
                cells[1].selectFirst("a")
            } else {
                null
            }
        }

        val rawStoreUrl = storeLinkElement?.attr("href") ?: ""
        val appId = steamAppIdRegex.find(rawStoreUrl)?.groupValues?.get(1) ?: "0"

        val titleElement = selectFirst("span.mdui-typo-title")
        val name = titleElement?.text()?.trim() ?: "Unknown"

        // Search for image matching the title
        val bg = select("img[src]").firstOrNull { img ->
            val alt = img.attr("alt").trim()
            val title = img.attr("title").trim()
            (alt.isNotEmpty() && alt == name) || (title.isNotEmpty() && title == name)
        }?.attr("src") ?: ""

        return SteamGalgame(
            appId = appId,
            name = name,
            bg = bg,
            link = link
        )
    }
}

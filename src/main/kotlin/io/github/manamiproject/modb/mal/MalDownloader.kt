package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.config.AnimeId
import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.downloader.Downloader
import io.github.manamiproject.modb.core.extensions.EMPTY
import io.github.manamiproject.modb.core.extensions.neitherNullNorBlank
import io.github.manamiproject.modb.core.extensions.pickRandom
import io.github.manamiproject.modb.core.httpclient.Browser.FIREFOX
import io.github.manamiproject.modb.core.httpclient.BrowserType.MOBILE
import io.github.manamiproject.modb.core.httpclient.DefaultHttpClient
import io.github.manamiproject.modb.core.httpclient.HttpClient
import io.github.manamiproject.modb.core.httpclient.RetryCase
import io.github.manamiproject.modb.core.httpclient.UserAgents
import io.github.manamiproject.modb.core.logging.LoggerDelegate

/**
 * Downloads anime data from myanimelist.net
 * @since 1.0.0
 * @param config Configuration for downloading data.
 * @param httpClient To actually download the anime data.
 */
public class MalDownloader(
    private val config: MetaDataProviderConfig,
    private val httpClient: HttpClient = DefaultHttpClient(isTestContext = config.isTestContext()).apply {
        retryBehavior.addCases(
            RetryCase { it.code == 403 },
            RetryCase { it.code == 404 && it.bodyAsText.contains("was not found on this server.</p>") },
        )
    },
) : Downloader {

    override suspend fun download(id: AnimeId, onDeadEntry: suspend (AnimeId) -> Unit): String {
        log.debug { "Downloading [malId=$id]" }

        val response = httpClient.get(
            url = config.buildDataDownloadLink(id).toURL(),
            headers = mapOf(USER_AGENT to listOf(UserAgents.userAgents(FIREFOX, MOBILE).pickRandom())),
        )

        check(response.bodyAsText.neitherNullNorBlank()) { "Response body was blank for [malId=$id] with response code [${response.code}]" }

        return when(response.code) {
            200 -> response.bodyAsText
            404 -> checkDeadEntry(id, onDeadEntry, response.bodyAsText)
            else -> throw IllegalStateException("Unable to determine the correct case for [malId=$id], [responseCode=${response.code}]")
        }
    }

    private suspend fun checkDeadEntry(malId: AnimeId, onDeadEntry: suspend (AnimeId) -> Unit, responseBody: String): String {
        return when {
            responseBody.contains("<title>404 Not Found - MyAnimeList.net") -> {
                onDeadEntry.invoke(malId)
                EMPTY
            }
            else -> throw IllegalStateException("Unknown 404 case for [malId=$malId]")
        }
    }

    private companion object {
        private val log by LoggerDelegate()
        private const val USER_AGENT = "User-Agent"
    }
}
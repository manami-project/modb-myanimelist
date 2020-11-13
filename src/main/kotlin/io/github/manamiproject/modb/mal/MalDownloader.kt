package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.config.AnimeId
import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.downloader.Downloader
import io.github.manamiproject.modb.core.excludeFromTestContext
import io.github.manamiproject.modb.core.extensions.EMPTY
import io.github.manamiproject.modb.core.httpclient.DefaultHttpClient
import io.github.manamiproject.modb.core.httpclient.HttpClient
import io.github.manamiproject.modb.core.httpclient.retry.RetryBehavior
import io.github.manamiproject.modb.core.httpclient.retry.RetryableRegistry
import io.github.manamiproject.modb.core.logging.LoggerDelegate
import io.github.manamiproject.modb.core.random

/**
 * Downloads anime data from myanimelist.net
 * @since 1.0.0
 * @param config Configuration for downloading data.
 * @param httpClient To actually download the anime data.
 */
public class MalDownloader(
    private val config: MetaDataProviderConfig,
    private val httpClient: HttpClient = DefaultHttpClient()
) : Downloader {

    init {
        registerRetryBehavior()
    }

    override fun download(id: AnimeId, onDeadEntry: (AnimeId) -> Unit): String {
        log.debug("Downloading [malId={}]", id)

        val response = httpClient.get(
            url = config.buildDataDownloadLink(id).toURL(),
            headers = mapOf(USER_AGENT to listOf(MOBILE_USER_AGENT)),
            retryWith = config.hostname()
        )

        check(response.body.isNotBlank()) { "Response body was blank for [malId=$id] with response code [${response.code}]" }

        return when(response.code) {
            200 -> response.body
            404 -> checkDeadEntry(id, onDeadEntry, response.body)
            else -> throw IllegalStateException("Unable to determine the correct case for [malId=$id], [responseCode=${response.code}]")
        }
    }

    private fun registerRetryBehavior() {
        val retryBehaviorConfig = RetryBehavior(
            waitDuration = { random(4000, 8000) },
            retryOnResponsePredicate = { httpResponse ->
                 listOf(403, 429, 500, 504).contains(httpResponse.code) || (httpResponse.code == 404 && httpResponse.body.contains("was not found on this server.</p>"))
            }
        ).apply {
            addExecuteBeforeRetryPredicate(403) {
                log.info("Crawler has been detected. Pausing for at least 5-8 minutes.")
                excludeFromTestContext(config) { Thread.sleep(random(296000, 400000)) }
            }
            addExecuteBeforeRetryPredicate(404) {
                log.info("Pausing before redownloading 404 candidate.")
            }
        }

        RetryableRegistry.register(config.hostname(), retryBehaviorConfig)
    }

    private fun checkDeadEntry(malId: AnimeId, onDeadEntry: (AnimeId) -> Unit, responseBody: String): String {
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
        private const val MOBILE_USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_15_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) FxiOS/29.0 Mobile/15E148 Safari/605.1.15"
    }
}
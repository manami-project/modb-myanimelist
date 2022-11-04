package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.config.AnimeId
import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.downloader.Downloader
import io.github.manamiproject.modb.core.excludeFromTestContextSuspendable
import io.github.manamiproject.modb.core.extensions.EMPTY
import io.github.manamiproject.modb.core.extensions.pickRandom
import io.github.manamiproject.modb.core.httpclient.Browser.FIREFOX
import io.github.manamiproject.modb.core.httpclient.BrowserType.MOBILE
import io.github.manamiproject.modb.core.httpclient.DefaultHttpClient
import io.github.manamiproject.modb.core.httpclient.HttpClient
import io.github.manamiproject.modb.core.httpclient.UserAgents
import io.github.manamiproject.modb.core.httpclient.retry.RetryBehavior
import io.github.manamiproject.modb.core.httpclient.retry.RetryableRegistry
import io.github.manamiproject.modb.core.logging.LoggerDelegate
import io.github.manamiproject.modb.core.random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.DurationUnit.MILLISECONDS
import kotlin.time.toDuration

/**
 * Downloads anime data from myanimelist.net
 * @since 1.0.0
 * @param config Configuration for downloading data.
 * @param httpClient To actually download the anime data.
 */
public class MalDownloader(
    private val config: MetaDataProviderConfig,
    private val httpClient: HttpClient = DefaultHttpClient(isTestContext = config.isTestContext())
) : Downloader {

    init {
        runBlocking {
            registerRetryBehavior()
        }
    }

    override suspend fun download(id: AnimeId, onDeadEntry: suspend (AnimeId) -> Unit): String {
        log.debug { "Downloading [malId=$id]" }

        val response = httpClient.get(
            url = config.buildDataDownloadLink(id).toURL(),
            headers = mapOf(USER_AGENT to listOf(UserAgents.userAgents(FIREFOX, MOBILE).pickRandom())),
            retryWith = config.hostname(),
        )

        check(response.body.isNotBlank()) { "Response body was blank for [malId=$id] with response code [${response.code}]" }

        return when(response.code) {
            200 -> response.body
            404 -> checkDeadEntry(id, onDeadEntry, response.body)
            else -> throw IllegalStateException("Unable to determine the correct case for [malId=$id], [responseCode=${response.code}]")
        }
    }

    private suspend fun registerRetryBehavior() {
        val retryBehavior = RetryBehavior(
            waitDuration = { random(4000, 8000).toDuration(MILLISECONDS) },
            isTestContext = config.isTestContext(),
        ).apply {
            addCase {
                it.code in setOf(429, 500, 504)
            }
            addCase(
                retryIf = { httpResponse -> httpResponse.code == 403 },
                executeBeforeRetry = {
                    log.info { "Crawler has been detected. Pausing for at least 6 minutes." }
                    excludeFromTestContextSuspendable(config) { delay(random(360000, 390000)) }
                }
            )
            addCase(
                retryIf = { httpResponse -> httpResponse.code == 404 && httpResponse.body.contains("was not found on this server.</p>") },
                executeBeforeRetry = { log.info { "Pausing before redownloading 404 candidate." } }
            )
        }

        RetryableRegistry.register(config.hostname(), retryBehavior)
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
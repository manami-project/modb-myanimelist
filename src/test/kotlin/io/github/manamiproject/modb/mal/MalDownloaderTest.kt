package io.github.manamiproject.modb.mal

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import io.github.manamiproject.modb.core.config.AnimeId
import io.github.manamiproject.modb.core.config.FileSuffix
import io.github.manamiproject.modb.core.config.Hostname
import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.extensions.EMPTY
import io.github.manamiproject.modb.core.extensions.toAnimeId
import io.github.manamiproject.modb.test.MockServerTestCase
import io.github.manamiproject.modb.test.WireMockServerCreator
import io.github.manamiproject.modb.test.exceptionExpected
import io.github.manamiproject.modb.test.shouldNotBeInvoked
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI

internal class MalDownloaderTest : MockServerTestCase<WireMockServer> by WireMockServerCreator() {

    @Nested
    inner class WaitAndRetryTests {

        @Test
        fun `response is 403 which indicates that a crawler has been detected - therefore pause and retry`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val responseBody = "<html><head/><body>Data</body></html>"

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs(STARTED)
                    .willSetStateTo("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(403)
                            .withBody("<html></html>")
                    )
            )

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(200)
                            .withBody(responseBody)
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

            // then
            assertThat(result).isEqualTo(responseBody)
        }

        @Test
        fun `falsely returning 404 - therefore pause and retry - successfully retrieve anime after retry`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val responseBody = "<html><head/><body></body></html>"

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs(STARTED)
                    .willSetStateTo("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(404)
                            .withBody("<html><head><title>404 Not Found - MyAnimeList.net</title><body><p>Death Note was not found on this server.</p></body></html>")
                    )
            )

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(200)
                            .withBody(responseBody)
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

            // then
            assertThat(result).isEqualTo(responseBody)
        }

        @Test
        fun `response is 429 'too many connections' - therefore has to pause and retry to download`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val responseBody = "<html><head/><body></body></html>"

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs(STARTED)
                    .willSetStateTo("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(429)
                            .withBody("<html></html>")
                    )
            )

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(200)
                            .withBody(responseBody)
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

            // then
            assertThat(result).isEqualTo(responseBody)
        }

        @Test
        fun `response is 500 'internal server error' - therefore has to pause and retry to download`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val responseBody = "<html><head/><body></body></html>"

            serverInstance.stubFor(
                    get(urlPathEqualTo("/anime/$id"))
                            .inScenario("pause and retry")
                            .whenScenarioStateIs(STARTED)
                            .willSetStateTo("successful retrieval")
                            .willReturn(
                                    aResponse()
                                            .withHeader("Content-Type", "text/html")
                                            .withStatus(500)
                                            .withBody("<html></html>")
                            )
            )

            serverInstance.stubFor(
                    get(urlPathEqualTo("/anime/$id"))
                            .inScenario("pause and retry")
                            .whenScenarioStateIs("successful retrieval")
                            .willReturn(
                                    aResponse()
                                            .withHeader("Content-Type", "text/html")
                                            .withStatus(200)
                                            .withBody(responseBody)
                            )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

            // then
            assertThat(result).isEqualTo(responseBody)
        }

        @Test
        fun `response is 504 'gateway timeout' - therefore has to pause and retry to download`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val responseBody = "<html><head/><body></body></html>"

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs(STARTED)
                    .willSetStateTo("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(504)
                            .withBody("<html></html>")
                    )
            )

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .inScenario("pause and retry")
                    .whenScenarioStateIs("successful retrieval")
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(200)
                            .withBody(responseBody)
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

            // then
            assertThat(result).isEqualTo(responseBody)
        }
    }

    @Nested
    inner class UnknownCaseTests {

        @Test
        fun `unhandled response code`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(502)
                            .withBody("<html></html>")
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = exceptionExpected<IllegalStateException> {
                malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() })
            }

            // then
            assertThat(result).hasMessage("Unable to determine the correct case for [malId=$id], [responseCode=502]")
        }

        @Test
        fun `responding 404 - unknown case`() {
            // given
            val id = 1535

            val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun hostname(): Hostname = "localhost"
                override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
                override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            serverInstance.stubFor(
                get(urlPathEqualTo("/anime/$id"))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "text/html")
                            .withStatus(404)
                            .withBody("<html><head/><body></body></html>")
                    )
            )

            val malDownloader = MalDownloader(testMalConfig)

            // when
            val result = exceptionExpected<IllegalStateException> {
                malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() })
            }

            // then
            assertThat(result).hasMessage("Unknown 404 case for [malId=$id]")
        }
    }

    @Test
    fun `successfully load an entry`() {
        // given
        val id = 1535

        val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
            override fun hostname(): Hostname = "localhost"
            override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
            override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
            override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
        }

        val responseBody = "<html><head/><body></body></html>"

        serverInstance.stubFor(
            get(urlPathEqualTo("/anime/$id")).willReturn(
                aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withStatus(200)
                    .withBody(responseBody)
            )
        )

        val malDownloader = MalDownloader(testMalConfig)

        // when
        val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { shouldNotBeInvoked() }) }

        // then
        assertThat(result).isEqualTo(responseBody)
    }

    @Test
    fun `responding 404 indicating dead entry - add to dead entry list and return empty string`() {
        // given
        val id = 1535
        var hasDeadEntryBeenInvoked = false

        val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
            override fun hostname(): Hostname = "localhost"
            override fun buildAnimeLink(id: AnimeId): URI = URI("http://localhost:$port/anime/$id")
            override fun buildDataDownloadLink(id: String): URI = buildAnimeLink(id)
            override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
        }

        serverInstance.stubFor(
            get(urlPathEqualTo("/anime/$id"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withStatus(404)
                        .withBody("<html><head><title>404 Not Found - MyAnimeList.net</title><body></body></html>")
                )
        )

        val malDownloader = MalDownloader(testMalConfig)

        // when
        val result = runBlocking { malDownloader.downloadSuspendable(id = id.toAnimeId(), onDeadEntry = { hasDeadEntryBeenInvoked = true }) }

        // then
        assertThat(hasDeadEntryBeenInvoked).isTrue()
        assertThat(result).isBlank()
    }

    @Test
    fun `throws an exception if the response body is empty`() {
        // given
        val id = 1535

        val testMalConfig = object: MetaDataProviderConfig by MetaDataProviderTestConfig {
            override fun hostname(): Hostname = "localhost"
            override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
            override fun buildDataDownloadLink(id: String): URI = URI("http://${hostname()}:$port/anime/$id")
            override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
        }

        serverInstance.stubFor(
            get(urlPathEqualTo("/anime/$id")).willReturn(
                aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withStatus(200)
                    .withBody(EMPTY)
            )
        )

        val downloader = MalDownloader(testMalConfig)

        // when
        val result = exceptionExpected<IllegalStateException> {
            downloader.downloadSuspendable(id.toAnimeId()) { shouldNotBeInvoked() }
        }

        // then
        assertThat(result).hasMessage("Response body was blank for [malId=1535] with response code [200]")
    }
}

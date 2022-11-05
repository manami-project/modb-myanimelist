package io.github.manamiproject.modb.mal

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import java.net.URI

internal class MalConfigTest {

    @Test
    fun `isTestContext is false`() {
        // when
        val result = MalConfig.isTestContext()

        // then
        assertThat(result).isFalse()
    }

    @Test
    fun `hostname must be correct`() {
        // when
        val result = MalConfig.hostname()

        // then
        assertThat(result).isEqualTo("myanimelist.net")
    }

    @Test
    fun `build anime link correctly`() {
        // given
        val id = "1535"

        // when
        val result = MalConfig.buildAnimeLink(id)

        // then
        assertThat(result).isEqualTo(URI("https://myanimelist.net/anime/$id"))
    }

    @Test
    fun `build data download link correctly`() {
        // given
        val id = "1535"

        // when
        val result = MalConfig.buildDataDownloadLink(id)

        // then
        assertThat(result).isEqualTo(URI("https://myanimelist.net/anime/$id"))
    }

    @Test
    fun `file suffix must be html`() {
        // when
        val result = MalConfig.fileSuffix()

        // then
        assertThat(result).isEqualTo("html")
    }
}

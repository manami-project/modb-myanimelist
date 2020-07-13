package io.github.manamiproject.modb.mal

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URL

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
    fun `build anime link URL correctly`() {
        // given
        val id = "1535"

        // when
        val result = MalConfig.buildAnimeLinkUrl(id)

        // then
        assertThat(result).isEqualTo(URL("https://${MalConfig.hostname()}/anime/$id"))
    }

    @Test
    fun `build data download URL correctly`() {
        // given
        val id = "1535"

        // when
        val result = MalConfig.buildDataDownloadUrl(id)

        // then
        assertThat(result).isEqualTo(URL("https://${MalConfig.hostname()}/anime/$id"))
    }

    @Test
    fun `file suffix must be html`() {
        // when
        val result = MalConfig.fileSuffix()

        // then
        assertThat(result).isEqualTo("html")
    }
}

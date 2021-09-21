package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.config.AnimeId
import io.github.manamiproject.modb.core.config.FileSuffix
import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.models.Anime
import io.github.manamiproject.modb.core.models.Anime.Status.*
import io.github.manamiproject.modb.core.models.Anime.Type.*
import io.github.manamiproject.modb.core.models.AnimeSeason.Season.*
import io.github.manamiproject.modb.core.models.Duration
import io.github.manamiproject.modb.core.models.Duration.TimeUnit.*
import io.github.manamiproject.modb.test.loadTestResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI

internal class MalConverterTest {

    @Nested
    inner class TitleTests {

        @Test
        fun `title containing special chars`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/title/special_chars.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.title).isEqualTo("Tobidasu PriPara: Mi~nna de Mezase! Idol☆Grand Prix")
        }

        @Test
        fun `anime has original and english title in header`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/title/english_and_original_title.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.title).isEqualTo("Rurouni Kenshin: Meiji Kenkaku Romantan")
        }
    }

    @Nested
    inner class TypeTests {

        @Test
        fun `type is TV`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/tv.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(TV)
        }

        @Test
        fun `'Unknown' is mapped to 'UNKNOWN'`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/unknown.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(Anime.Type.UNKNOWN)
        }

        @Test
        fun `type is Movie`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/movie.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(MOVIE)
        }

        @Test
        fun `type is music is mapped to special`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/music.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(SPECIAL)
        }

        @Test
        fun `type is ONA`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/ona.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(ONA)
        }

        @Test
        fun `type is OVA`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/ova.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(OVA)
        }

        @Test
        fun `type is Special`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/special.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(SPECIAL)
        }

        @Test
        fun `movie case which resulted in containsOwn`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/type/movie_case_which_resulted_in_containsOwn.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.type).isEqualTo(MOVIE)
        }
    }

    @Nested
    inner class EpisodesTests {

        @Test
        fun `unknown number of episodes`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/episodes/unknown.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.episodes).isEqualTo(0)
        }

        @Test
        fun `1 episode`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/episodes/1.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.episodes).isOne()
        }

        @Test
        fun `10 episodes`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/episodes/10.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.episodes).isEqualTo(10)
        }

        @Test
        fun `100 episodes`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/episodes/100.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.episodes).isEqualTo(100)
        }

        @Test
        fun `1818 episodes`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/episodes/1818.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.episodes).isEqualTo(1818)
        }
    }

    @Nested
    inner class PictureAndThumbnailTests {

        @Test
        fun `neither picture nor thumbnail`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/picture_and_thumbnail/neither_picture_nor_thumbnail.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.picture).isEqualTo(URI("https://cdn.myanimelist.net/images/qm_50.gif"))
            assertThat(result.thumbnail).isEqualTo(URI("https://cdn.myanimelist.net/images/qm_50.gif"))
        }

        @Test
        fun `picture and thumbnail`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/picture_and_thumbnail/picture_and_thumbnail_available.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.picture).isEqualTo(URI("https://cdn.myanimelist.net/images/anime/5/50551.jpg"))
            assertThat(result.thumbnail).isEqualTo(URI("https://cdn.myanimelist.net/images/anime/5/50551t.jpg"))
        }
    }

    @Nested
    inner class SynonymsTests {

        @Test
        fun `no synonyms`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/no_synonyms.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).isEmpty()
        }

        @Test
        fun `one synonym`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/one_synonym.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly("エスパー魔美スペシャル マイエンジェル魔美ちゃん")
        }

        @Test
        fun `multiple languages, one synonym each`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/multiple_languages_one_each.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "Prétear: The New Legend of Snow White",
                "Shin Shirayuki-hime Densetsu Pretear",
                "新白雪姫伝説プリーティア"
            )
        }

        @Test
        fun `synonym containing comma`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/synonym_contains_comma_but_title_does_not.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "Steins;Gate Special",
                "Steins;Gate: Egoistic Poriomania",
                "シュタインズ ゲート 横行跋扈のポリオマニア"
            )
        }

        @Test
        fun `multiple synonyms for one language`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/multiple_synonyms_for_one_language.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "DB Kai",
                "DBK",
                "DBZ Kai",
                "Dragon Ball Z Kai: The Final Chapters",
                "Dragonball Kai",
                "ドラゴンボール改"
            )
        }

        @Test
        fun `multiple synonyms with comma in one language section`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/multiple_synonyms_with_comma_in_one_language.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "Kono Naka ni Hitori, Imouto ga Iru! Episode 13",
                "Kono Naka ni Hitori, Imouto ga Iru! OVA",
                "NAKAIMO - My Little Sister Is Among Them! OVA",
                "NakaImo, One of Them is My Younger Sister! OVA",
                "Who is Imouto? OVA",
                "この中に1人、妹がいる！兄、妹、恋人"
            )
        }

        @Test
        fun `one synonym with multiple commas`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/one_synonym_with_multiple_commas.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "Yuru Yuri Special",
                "YuruYuri: For Whatever Reason, Never Ending, Heart Throbbing, Pitter-patter, Paradox Eternal",
                "ゆるゆり どうして☆止まらない☆トキメキ☆ドキドキ☆パラドクス☆エターナル"
            )
        }

        @Test
        fun `semicolon in synonym wihtout whitespaces`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/semicolon_in_synonym_wihtout_whitespaces.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "CHAOS;CHILD SILENT SKY",
                "Chaos Child Episode 13",
                "Chaos Child Episode 14",
                "Chaos Child: Silent Sky"
            )
        }

        @Test
        fun `semicolon in synonym with whitespace`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/semicolon_in_synonym_with_whitespace.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "劇場版Fate/Grand Order -神聖円卓領域キャメロット- Wandering; Agateram"
            )
        }

        @Test
        fun `semicolon in title but not in synonyms`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/synonyms/semicolon_in_title_but_not_in_synonyms.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.synonyms).containsExactly(
                "Dual Parallel! Trouble Adventures - Final Frontier",
                "Dual! Parallel Lunlun Monogatari Special",
                "Dual! Parallel Trouble Adventures Special",
                "デュアル！ぱられルンルン物語「ファイナル・フロンティア」"
            )
        }
    }

    @Nested
    inner class SourcesTests {

        @Test
        fun `extract id 16498`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/sources/16498.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.sources).containsExactly(URI("https://myanimelist.net/anime/16498"))
        }
    }

    @Nested
    inner class RelatedAnimeTests {

        @Test
        fun `no adaption, no relations`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/related_anime/no_adaption_no_relations.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.relatedAnime).isEmpty()
        }

        @Test
        fun `no adaption, multiple relations`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/related_anime/no_adaption_multiple_relations.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.relatedAnime).containsExactly(
                URI("https://myanimelist.net/anime/17819"),
                URI("https://myanimelist.net/anime/19671"),
                URI("https://myanimelist.net/anime/22265"),
                URI("https://myanimelist.net/anime/30415"),
                URI("https://myanimelist.net/anime/33845")
            )
        }

        @Test
        fun `one adaption, one relation`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/related_anime/has_one_adaption_and_one_relation.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.relatedAnime).containsExactly(
                URI("https://myanimelist.net/anime/14367")
            )
        }

        @Test
        fun `has adaption, multiple relations`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/related_anime/has_adaption_and_multiple_relations.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.relatedAnime).containsExactly(
                URI("https://myanimelist.net/anime/12685"),
                URI("https://myanimelist.net/anime/15197"),
                URI("https://myanimelist.net/anime/15199"),
                URI("https://myanimelist.net/anime/15201"),
                URI("https://myanimelist.net/anime/17277"),
                URI("https://myanimelist.net/anime/1953"),
                URI("https://myanimelist.net/anime/2124"),
                URI("https://myanimelist.net/anime/2904"),
                URI("https://myanimelist.net/anime/30711"),
                URI("https://myanimelist.net/anime/33155"),
                URI("https://myanimelist.net/anime/34438"),
                URI("https://myanimelist.net/anime/34439"),
                URI("https://myanimelist.net/anime/40334"),
                URI("https://myanimelist.net/anime/40836"),
                URI("https://myanimelist.net/anime/41075"),
                URI("https://myanimelist.net/anime/4596"),
                URI("https://myanimelist.net/anime/8888"),
            )
        }

        @Test
        fun `has adaption, no relations`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/related_anime/has_adaption_but_no_relation.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.relatedAnime).isEmpty()
        }
    }

    @Nested
    inner class StatusTests {

        @Test
        fun `'currently airing' is mapped to 'CURRENTLY_AIRING'`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/status/currently_airing.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.status).isEqualTo(ONGOING)
        }

        @Test
        fun `'Not yet aired' is mapped to 'NOT_YET_AIRED'`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/status/not_yet_aired.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.status).isEqualTo(UPCOMING)
        }

        @Test
        fun `'Finished Airing' is mapped to 'FINISHED_AIRING'`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/status/finished_airing.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.status).isEqualTo(FINISHED)
        }
    }

    @Nested
    inner class TagsTests {

        @Test
        fun `extract multiple tags`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/tags/multiple_tags.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.tags).containsExactly(
                "action",
                "adventure",
                "comedy",
                "drama",
                "fantasy",
                "military",
                "shounen"
            )
        }

        @Test
        fun `extract exactly one tag`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/tags/one_tag.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.tags).containsExactly("fantasy")
        }

        @Test
        fun `no tags available`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/tags/no_tags.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.tags).isEmpty()
        }
    }

    @Nested
    inner class DurationTests {

        @Test
        fun `1 hr`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/1_hour.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(1, HOURS))
        }

        @Test
        fun `1 hr 11 min`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/1_hour_11_min.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(71, MINUTES))
        }

        @Test
        fun `1 hr 11 min per ep`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/1_hour_11_min_per_episode.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(71, MINUTES))
        }

        @Test
        fun `2 hr`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/2_hours.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(2, HOURS))
        }

        @Test
        fun `2 hr 15 min`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/2_hours_15_minutes.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(135, MINUTES))
        }

        @Test
        fun `10 min`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/10_min.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(10, MINUTES))
        }

        @Test
        fun `10 min per ep`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/10_min_per_episode.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(10, MINUTES))
        }

        @Test
        fun `10 sec`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/10_sec.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(10, SECONDS))
        }

        @Test
        fun `10 sec per ep`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/10_sec_per_episode.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(10, SECONDS))
        }

        @Test
        fun `unknown duration`() {
            // given
            val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
            }

            val testFile = loadTestResource("file_converter_tests/duration/unknown.html")

            val converter = MalConverter(testMalConfig)

            // when
            val result = converter.convert(testFile)

            // then
            assertThat(result.duration).isEqualTo(Duration(0, SECONDS))
        }
    }

    @Nested
    inner class AnimeSeasonTests {

        @Nested
        inner class SeasonTests {

            @Test
            fun `'UNDEFINED', because anime season link is not available`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/season/undefined.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.season).isEqualTo(UNDEFINED)
            }

            @Test
            fun `season is 'FALL'`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/season/fall.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.season).isEqualTo(FALL)
            }

            @Test
            fun `season is 'SPRING'`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/season/spring.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.season).isEqualTo(SPRING)
            }

            @Test
            fun `season is 'SUMMER'`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/season/summer.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.season).isEqualTo(SUMMER)
            }

            @Test
            fun `season is 'WINTER'`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/season/winter.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.season).isEqualTo(WINTER)
            }
        }

        @Nested
        inner class YearOfPremiereTests {

            @Test
            fun `extract from anime season link which exists in mobile version, but not on desktop version`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/anime_season_link_mobile_only.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2010)
            }

            @Test
            fun `extract from anime season link which exists on both mobile and desktop version`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/anime_season_link_on_mobile_and_desktop.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2020)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - '2012-07-30 - unknown`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2012-07-30_-_unknown.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2012)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2018-10-10 - 2018-12-20`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2018-10-10_-_2018-12-20.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2018)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2019-02-21`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2019-02-21.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2019)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 1982-10-09 - 1983`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/1982-10-09_-_1983.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(1982)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 1992-10 - 1993`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/1992-10_-_1993.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(1992)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2018-09 - unknown`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2018-09_-_unknown.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2018)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2002`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2002.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2002)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2008-08 - 2008`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2008-08_-_2008.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2008)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 1964 - 1965`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/1964_-_1965.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(1964)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2011-04 - 2011-05`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2011-04_-_2011-05.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2011)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2010 - 2010-03`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2010_-_2010-03.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2010)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 1999 - 2000-05-22`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/1999_-_2000-05-22.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(1999)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2013-01`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2013-01.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2013)
            }

            @Test
            fun `extract from 'aired', because anime season is not set - 2021 - unknown`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/2021_-_unknown.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(2021)
            }

            @Test
            fun `neither anime season nor 'aired' is available`() {
                // given
                val testMalConfig = object : MetaDataProviderConfig by MetaDataProviderTestConfig {
                    override fun buildAnimeLink(id: AnimeId): URI = MalConfig.buildAnimeLink(id)
                    override fun buildDataDownloadLink(id: String): URI = MalConfig.buildDataDownloadLink(id)
                    override fun fileSuffix(): FileSuffix = MalConfig.fileSuffix()
                }

                val testFile = loadTestResource("file_converter_tests/anime_season/year_of_premiere/neither_premiered_nor_aired_available.html")

                val converter = MalConverter(testMalConfig)

                // when
                val result = converter.convert(testFile)

                // then
                assertThat(result.animeSeason.year).isEqualTo(0)
            }
        }
    }
}
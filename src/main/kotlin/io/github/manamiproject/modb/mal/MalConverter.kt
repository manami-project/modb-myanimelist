package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.config.MetaDataProviderConfig
import io.github.manamiproject.modb.core.converter.AnimeConverter
import io.github.manamiproject.modb.core.extensions.EMPTY
import io.github.manamiproject.modb.core.logging.LoggerDelegate
import io.github.manamiproject.modb.core.models.Anime
import io.github.manamiproject.modb.core.models.Anime.Status
import io.github.manamiproject.modb.core.models.Anime.Status.*
import io.github.manamiproject.modb.core.models.Anime.Type
import io.github.manamiproject.modb.core.models.Anime.Type.*
import io.github.manamiproject.modb.core.models.AnimeSeason
import io.github.manamiproject.modb.core.models.AnimeSeason.Season
import io.github.manamiproject.modb.core.models.Duration
import io.github.manamiproject.modb.core.models.Duration.TimeUnit.SECONDS
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

/**
 * Converts raw data to an [Anime].
 * Requires raw HTML from the mobile site version.
 * @since 1.0.0
 * @param config Configuration for converting data.
 */
public class MalConverter(
    private val config: MetaDataProviderConfig = MalConfig
) : AnimeConverter {

    override fun convert(rawContent: String): Anime {
        val document = Jsoup.parse(rawContent)

        val picture = extractPicture(document)
        val title = extractTitle(document)

        val synonyms = postProcessSynonyms(title, extractSynonyms(document))

        return Anime(
            _title = title,
            episodes = extractEpisodes(document),
            type = extractType(document),
            picture = picture,
            thumbnail = findThumbnail(picture),
            status = extractStatus(document),
            duration = extractDuration(document),
            animeSeason = extractAnimeSeason(document)
        ).apply {
            addSources(extractSourcesEntry(document))
            addSynonyms(synonyms)
            addRelations(extractRelatedAnime(document))
            addTags(extractTags(document))
        }
    }

    private fun postProcessSynonyms(title: String, synonyms: List<String>): List<String> {
        val processedSynonyms = mutableListOf<String>()

        when {
            !title.contains(';')  -> {
                processedSynonyms.addAll(
                    synonyms.flatMap { it.split("; ") }
                        .map { it.replace(";", EMPTY) }
                        .map { it.trim() }
                )
            }
            title.contains(Regex("[^ ];[^ ]")) or title.endsWith(";") -> {
                processedSynonyms.addAll(
                    synonyms.flatMap { it.split("; ") }
                        .map { it.trim() }
                )
            }
            else -> processedSynonyms.addAll(synonyms.map { it.trim() })
        }

        return processedSynonyms
    }

    private fun extractTitle(document: Document) = document.select("meta[property=og:title]").attr("content").trim()

    private fun extractEpisodes(document: Document): Int {
        val text = document.select("td:containsOwn(Episodes)").next().select("a").text().trim()

        val matchResult = Regex("[0-9]+").find(text)

        return matchResult?.value?.toInt() ?: 0
    }

    private fun extractType(document: Document): Type {
        val text = document.select("td:containsOwn(Type)").next().select("a").text().trim()

        return when(text) {
            "TV" -> TV
            "Unknown" -> TV
            "Movie" -> Movie
            "OVA" -> OVA
            "ONA" -> ONA
            "Special" -> Special
            "Music" -> Special
            else -> throw IllegalStateException("Unknown type detected")
        }
    }

    private fun extractPicture(document: Document): URL {
        val text = document.select("div[class=status-block] > div[class=icon-thumb thumbs-zoom]").attr("data-image").trim()

        return if (text == "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png") {
            URL("https://cdn.myanimelist.net/images/qm_50.gif")
        } else {
            URL(text)
        }
    }

    private fun findThumbnail(picture: URL): URL {
        return if ("https://cdn.myanimelist.net/images/qm_50.gif" != picture.toString()) {
            URL(picture.toString().replace(".jpg", "t.jpg"))
        } else {
            picture
        }
    }

    private fun extractSynonyms(document: Document): List<String> {
        return document.select("h2:containsOwn(Information)")
            .next()
            .select("tr")
            .first()
            .select("td")[1]
            .textNodes()
            .map { it.text() }
            .filter { it.isNotBlank() }
    }

    private fun extractSourcesEntry(document: Document): List<URL> {
        val text = document.select("meta[property=og:url]").attr("content").trim()
        val matchResult = Regex("/[0-9]+/").find(text)
        val rawId = matchResult?.value ?: throw IllegalStateException("Unable to extract source")
        val id = rawId.trimStart('/').trimEnd('/')

        return listOf(config.buildAnimeLinkUrl(id))
    }

    private fun extractRelatedAnime(document: Document): List<URL> {
        return document.select("h2:containsOwn(Related Anime)").next().select("table > tbody > tr")
            .filterNot { it.text().trim().startsWith("Adaptation") }
            .flatMap { it.select("a") }
            .map { it.attr("href") }
            .mapNotNull { Regex("[0-9]+").find(it)?.value }
            .map { config.buildAnimeLinkUrl(it) }
    }

    private fun extractStatus(document: Document): Status {
        val status = document.select("td:containsOwn(Status)").next().text().trim()

        return when(status) {
            "Finished Airing" -> FINISHED
            "Currently Airing" -> CURRENTLY
            "Not yet aired" -> UPCOMING
            else -> throw IllegalStateException("Unknown status [$status}]")
        }
    }

    private fun extractDuration(document: Document): Duration {
        val text = document.select("td:containsOwn(Duration)").next().text().trim()

        val values = Regex("[0-9]+").findAll(text).toList().map { it.value }
        val units = Regex("(hr|min|sec)").findAll(text)
            .toList()
            .map { it.value }
            .map { it.trim() }
            .map { it.toLowerCase() }

        if (values.count() != units.count()) {
            log.warn("The amount of values [{}] does not match the amount of units [{}].", values.count(), units.count())
            return Duration(0, SECONDS)
        }

        val valueUnitPairs = mutableListOf<Pair<Int, String>>()

        for (index in values.indices) {
            valueUnitPairs.add(Pair(values[index].toInt(), units[index]))
        }

        var durationInSeconds = 0

        valueUnitPairs.forEach {
            durationInSeconds += when (it.second) {
                "sec" -> it.first
                "min" -> it.first * 60
                "hr" -> it.first * 3600
                else -> throw IllegalStateException("[${it.second} is an unknown unit.")
            }
        }

        return Duration(durationInSeconds, SECONDS)
    }

    private fun extractAnimeSeason(document: Document): AnimeSeason {
        val text = document.select("a[class=tag-premiered]").text()

        val seasonText = Regex("[aA-zZ]+").find(text)?.value ?: EMPTY
        val season =  Season.of(seasonText)

        val yearPremiered = Regex("[0-9]{4}").find(text)?.value?.toInt() ?: 0
        val year = if (yearPremiered != 0) {
            yearPremiered
        } else {
            exractRegularYear(document)
        }

        return AnimeSeason(
            season = season,
            _year = year
        )
    }

    private fun exractRegularYear(document: Document): Int {
        val text = document.select("td:containsOwn(Aired)").next().text().trim()
        return  Regex("[0-9]{4}").findAll(text).firstOrNull()?.value?.toInt() ?: 0
    }

    private fun extractTags(document: Document): List<String> {
        return document.select("td:containsOwn(Genres)")
            .next()
            .select("a")
            .eachText()
    }

    private companion object {
        private val log by LoggerDelegate()
    }
}
package io.github.manamiproject.modb.mal

import io.github.manamiproject.modb.core.extensions.writeToFileSuspendable
import io.github.manamiproject.modb.test.testResource
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val downloader = MalDownloader(MalConfig)
    
    runBlocking { 
        downloader.downloadSuspendable("38483").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/season/fall.html"))
        downloader.downloadSuspendable("38000").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/season/spring.html"))
        downloader.downloadSuspendable("37347").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/season/summer.html"))
        downloader.downloadSuspendable("40028").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/season/undefined.html"))
        downloader.downloadSuspendable("37779").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/season/winter.html"))
    
        downloader.downloadSuspendable("8663").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/1964_-_1965.html"))
        downloader.downloadSuspendable("11307").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/1982-10-09_-_1983.html"))
        downloader.downloadSuspendable("36736").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/1992-10_-_1993.html"))
        downloader.downloadSuspendable("4369").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/1999_-_2000-05-22.html"))
        downloader.downloadSuspendable("10262").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2002.html"))
        downloader.downloadSuspendable("5241").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2008-08_-_2008.html"))
        downloader.downloadSuspendable("41415").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2010_-_2010-03.html"))
        downloader.downloadSuspendable("41417").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2011-04_-_2011-05.html"))
        downloader.downloadSuspendable("32818").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2012-07-30_-_unknown.html"))
        downloader.downloadSuspendable("41717").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2013-01.html"))
        downloader.downloadSuspendable("40777").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2018-09_-_unknown.html"))
        downloader.downloadSuspendable("39462").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2018-10-10_-_2018-12-20.html"))
        downloader.downloadSuspendable("39727").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2019-02-21.html"))
        downloader.downloadSuspendable("41265").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/2021_-_unknown.html"))
        downloader.downloadSuspendable("12659").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/anime_season_link_mobile_only.html"))
        downloader.downloadSuspendable("39547").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/anime_season_link_on_mobile_and_desktop.html"))
        downloader.downloadSuspendable("23275").writeToFileSuspendable(resourceFile("file_converter_tests/anime_season/year_of_premiere/neither_premiered_nor_aired_available.html"))
    
        downloader.downloadSuspendable("10056").writeToFileSuspendable(resourceFile("file_converter_tests/duration/1_hour.html"))
        downloader.downloadSuspendable("10821").writeToFileSuspendable(resourceFile("file_converter_tests/duration/1_hour_11_min.html"))
        downloader.downloadSuspendable("10937").writeToFileSuspendable(resourceFile("file_converter_tests/duration/1_hour_11_min_per_episode.html"))
        downloader.downloadSuspendable("10389").writeToFileSuspendable(resourceFile("file_converter_tests/duration/2_hours.html"))
        downloader.downloadSuspendable("1091").writeToFileSuspendable(resourceFile("file_converter_tests/duration/2_hours_15_minutes.html"))
        downloader.downloadSuspendable("10055").writeToFileSuspendable(resourceFile("file_converter_tests/duration/10_min.html"))
        downloader.downloadSuspendable("10039").writeToFileSuspendable(resourceFile("file_converter_tests/duration/10_min_per_episode.html"))
        downloader.downloadSuspendable("31686").writeToFileSuspendable(resourceFile("file_converter_tests/duration/10_sec.html"))
        downloader.downloadSuspendable("32737").writeToFileSuspendable(resourceFile("file_converter_tests/duration/10_sec_per_episode.html"))
        downloader.downloadSuspendable("10506").writeToFileSuspendable(resourceFile("file_converter_tests/duration/unknown.html"))
    
        downloader.downloadSuspendable("31758").writeToFileSuspendable(resourceFile("file_converter_tests/episodes/1.html"))
        downloader.downloadSuspendable("851").writeToFileSuspendable(resourceFile("file_converter_tests/episodes/10.html"))
        downloader.downloadSuspendable("2165").writeToFileSuspendable(resourceFile("file_converter_tests/episodes/100.html"))
        downloader.downloadSuspendable("12393").writeToFileSuspendable(resourceFile("file_converter_tests/episodes/1818.html"))
        downloader.downloadSuspendable("30088").writeToFileSuspendable(resourceFile("file_converter_tests/episodes/unknown.html"))
    
        downloader.downloadSuspendable("41057").writeToFileSuspendable(resourceFile("file_converter_tests/picture_and_thumbnail/neither_picture_nor_thumbnail.html"))
        downloader.downloadSuspendable("10163").writeToFileSuspendable(resourceFile("file_converter_tests/picture_and_thumbnail/picture_and_thumbnail_available.html"))
    
        downloader.downloadSuspendable("1575").writeToFileSuspendable(resourceFile("file_converter_tests/related_anime/has_adaption_and_multiple_relations.html"))
        downloader.downloadSuspendable("25397").writeToFileSuspendable(resourceFile("file_converter_tests/related_anime/has_adaption_but_no_relation.html"))
        downloader.downloadSuspendable("200").writeToFileSuspendable(resourceFile("file_converter_tests/related_anime/has_one_adaption_and_one_relation.html"))
        downloader.downloadSuspendable("18507").writeToFileSuspendable(resourceFile("file_converter_tests/related_anime/no_adaption_multiple_relations.html"))
        downloader.downloadSuspendable("10003").writeToFileSuspendable(resourceFile("file_converter_tests/related_anime/no_adaption_no_relations.html"))
    
        downloader.downloadSuspendable("16498").writeToFileSuspendable(resourceFile("file_converter_tests/sources/16498.html"))
    
        downloader.downloadSuspendable("40839").writeToFileSuspendable(resourceFile("file_converter_tests/status/currently_airing.html"))
        downloader.downloadSuspendable("21329").writeToFileSuspendable(resourceFile("file_converter_tests/status/finished_airing.html"))
        downloader.downloadSuspendable("39783").writeToFileSuspendable(resourceFile("file_converter_tests/status/not_yet_aired.html"))
    
        downloader.downloadSuspendable("100").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/multiple_languages_one_each.html"))
        downloader.downloadSuspendable("22777").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/multiple_synonyms_for_one_language.html"))
        downloader.downloadSuspendable("15609").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/multiple_synonyms_with_comma_in_one_language.html"))
        downloader.downloadSuspendable("30559").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/no_synonyms.html"))
        downloader.downloadSuspendable("10000").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/one_synonym.html"))
        downloader.downloadSuspendable("12665").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/one_synonym_with_multiple_commas.html"))
        downloader.downloadSuspendable("35315").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/semicolon_in_synonym_wihtout_whitespaces.html"))
        downloader.downloadSuspendable("38085").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/semicolon_in_synonym_with_whitespace.html"))
        downloader.downloadSuspendable("993").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/semicolon_in_title_but_not_in_synonyms.html"))
        downloader.downloadSuspendable("10863").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/synonym_contains_comma_but_title_does_not.html"))
        downloader.downloadSuspendable("12665").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/synonym_contains_comma_followed_by_whitespace.html"))
        downloader.downloadSuspendable("15609").writeToFileSuspendable(resourceFile("file_converter_tests/synonyms/title_contains_comma_and_multiple_synonyms_for_one_language.html"))
    
        downloader.downloadSuspendable("5114").writeToFileSuspendable(resourceFile("file_converter_tests/tags/multiple_tags.html"))
        downloader.downloadSuspendable("28487").writeToFileSuspendable(resourceFile("file_converter_tests/tags/no_tags.html"))
        downloader.downloadSuspendable("10077").writeToFileSuspendable(resourceFile("file_converter_tests/tags/one_tag.html"))
    
        downloader.downloadSuspendable("45").writeToFileSuspendable(resourceFile("file_converter_tests/title/english_and_original_title.html"))
        downloader.downloadSuspendable("31055").writeToFileSuspendable(resourceFile("file_converter_tests/title/special_chars.html"))
    
        downloader.downloadSuspendable("28851").writeToFileSuspendable(resourceFile("file_converter_tests/type/movie.html"))
        downloader.downloadSuspendable("30097").writeToFileSuspendable(resourceFile("file_converter_tests/type/movie_case_which_resulted_in_containsOwn.html"))
        downloader.downloadSuspendable("12659").writeToFileSuspendable(resourceFile("file_converter_tests/type/music.html"))
        downloader.downloadSuspendable("38935").writeToFileSuspendable(resourceFile("file_converter_tests/type/ona.html"))
        downloader.downloadSuspendable("44").writeToFileSuspendable(resourceFile("file_converter_tests/type/ova.html"))
        downloader.downloadSuspendable("21329").writeToFileSuspendable(resourceFile("file_converter_tests/type/special.html"))
        downloader.downloadSuspendable("1535").writeToFileSuspendable(resourceFile("file_converter_tests/type/tv.html"))
        downloader.downloadSuspendable("24023").writeToFileSuspendable(resourceFile("file_converter_tests/type/unknown.html"))
    }
}

private fun resourceFile(file: String): Path {
    return Paths.get(
        testResource(file).toAbsolutePath()
            .toString()
            .replace("/build/resources/test/", "/src/test/resources/")
    )
}
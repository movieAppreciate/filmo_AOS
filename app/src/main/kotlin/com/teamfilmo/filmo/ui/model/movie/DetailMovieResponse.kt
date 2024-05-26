package com.teamfilmo.filmo.ui.model.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailMovieResponse(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("belongs_to_collection")
    val belongsToCollection: Boolean?,
    @SerialName("budget")
    val budget: Int,
    @SerialName("genres")
    val genres: List<Genre>,
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerialName("providers")
    val providers: MovieProviders,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("revenue")
    val revenue: Int,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
)

@Serializable
data class Genre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class ProductionCompany(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String,
)

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String,
)

@Serializable(with = MovieSerializer::class)
sealed class MovieProviders {
    @Serializable
    data class Providers1(
        @SerialName("link") val link: String,
        @SerialName("flatrate") val flatrate: List<Info>?,
        @SerialName("buy") val buy: List<Info>?,
        @SerialName("rent") val rent: List<Info>?,
    ) : MovieProviders()

    @Serializable
    data class Providers2(
        @SerialName("link") val link: String,
        @SerialName("ads") val ads: List<Info>?,
        @SerialName("free") val free: List<Info>?,
    ) : MovieProviders()
}

@Serializable
data class Info(
    @SerialName("logo_path")
    val logoPath: String,
    @SerialName("provider_id")
    val providerId: Int,
    @SerialName("provider_name")
    val providerName: String,
    @SerialName("display_priority")
    val displayPriority: Int,
)

@Serializable
data class SpokenLanguage(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String,
)
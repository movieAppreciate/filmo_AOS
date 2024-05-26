package com.teamfilmo.filmo.domain.repository

import com.teamfilmo.filmo.ui.model.movie.DetailMovieResponse

interface MovieRepository {
    suspend fun searchList(
        query: String,
        page: Int,
    ): Result<String>

    suspend fun searchDetail(movieId: Int): Result<DetailMovieResponse>

    suspend fun getVideo(movieId: Int): Result<String>

    suspend fun getPoster(movieId: Int): Result<String>
}

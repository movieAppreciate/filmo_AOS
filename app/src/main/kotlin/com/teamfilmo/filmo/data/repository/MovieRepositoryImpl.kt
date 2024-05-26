package com.teamfilmo.filmo.data.repository

import com.teamfilmo.filmo.data.source.MovieDataSource
import com.teamfilmo.filmo.domain.repository.MovieRepository
import com.teamfilmo.filmo.ui.model.movie.DetailMovieResponse
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val movieDataSource: MovieDataSource,
    ) : MovieRepository {
        override suspend fun searchList(
            query: String,
            page: Int,
        ): Result<String> {
            return movieDataSource.searchList(query, page)
        }

        override suspend fun searchDetail(movieId: Int): Result<DetailMovieResponse> {
            return movieDataSource.searchDetail(movieId)
        }

        override suspend fun getVideo(movieId: Int): Result<String> {
            return movieDataSource.getVideo(movieId)
        }

        override suspend fun getPoster(movieId: Int): Result<String> {
            return movieDataSource.getPoster(movieId)
        }
    }

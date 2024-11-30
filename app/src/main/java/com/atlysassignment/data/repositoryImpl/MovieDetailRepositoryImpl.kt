package com.atlysassignment.data.repositoryImpl

import com.atlysassignment.data.mapper.MovieMapper
import com.atlysassignment.data.remote.MovieApiService
import com.atlysassignment.domain.repository.MovieDetailRepository
import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState

class MovieDetailRepositoryImpl(
    private val movieApiService: MovieApiService
): MovieDetailRepository {
    override suspend fun fetchMovieDetails(movieId: String): NetworkState<Movie> {
        return try {
            val response = movieApiService.retrieveMovieDetail(movieId)
            val result = response.body()

            if (response.isSuccessful && result != null) {
                NetworkState.Success(MovieMapper.mapDetails(result))
            } else {
                NetworkState.Error("Something went wrong")
            }
        } catch (exception: Exception) {
            NetworkState.Error("Error occurred:- ${exception.localizedMessage}")
        }
    }
}
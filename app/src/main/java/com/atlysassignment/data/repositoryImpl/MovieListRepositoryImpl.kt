package com.atlysassignment.data.repositoryImpl

import com.atlysassignment.data.mappers.MovieListMapper
import com.atlysassignment.data.remote.MovieApiService
import com.atlysassignment.domain.repository.MovieListRepository
import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState

class MovieListRepositoryImpl(
    private val movieApiService: MovieApiService
): MovieListRepository {
    override suspend fun fetchMovieList(): NetworkState<List<Movie>> {
        return try {
            val response = movieApiService.retrieveMovieList()
            val result = response.body()

            if (response.isSuccessful && result != null) {
                NetworkState.Success(MovieListMapper.map(result))
            } else {
                NetworkState.Error("Something went wrong")
            }
        } catch (exception: Exception) {
            NetworkState.Error("Error occurred:- ${exception.localizedMessage}")
        }
    }
}
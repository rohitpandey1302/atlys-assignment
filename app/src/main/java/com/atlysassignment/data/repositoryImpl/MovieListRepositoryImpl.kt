package com.atlysassignment.data.repositoryImpl

import com.atlysassignment.data.mapper.MovieMapper
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
                NetworkState.Success(MovieMapper.mapList(result))
            } else {
                NetworkState.Error("Something went wrong")
            }
        } catch (exception: Exception) {
            NetworkState.Error("Error occurred:- ${exception.localizedMessage}")
        }
    }

    override suspend fun searchMovie(query: String): NetworkState<List<Movie>> {
        return try {
            val response = movieApiService.searchMovie(query)
            val result = response.body()

            if (response.isSuccessful && result != null) {
                NetworkState.Success(MovieMapper.mapList(result))
            } else {
                NetworkState.Error("Something went wrong")
            }
        } catch (exception: Exception) {
            NetworkState.Error("Error occurred:- ${exception.localizedMessage}")
        }
    }
}
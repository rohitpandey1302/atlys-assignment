package com.atlysassignment.domain.repository

import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState

interface MovieListRepository {
    suspend fun fetchMovieList(): NetworkState<List<Movie>>
    suspend fun searchMovie(query: String): NetworkState<List<Movie>>
}
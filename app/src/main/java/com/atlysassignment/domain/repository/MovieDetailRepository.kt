package com.atlysassignment.domain.repository

import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState

interface MovieDetailRepository {
    suspend fun fetchMovieDetails(movieId: String): NetworkState<Movie>
}
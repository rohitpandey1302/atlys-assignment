package com.atlysassignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlysassignment.data.mapper.MovieMapper
import com.atlysassignment.domain.repository.MovieDetailRepository
import com.atlysassignment.domain.repository.MovieListRepository
import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState
import com.atlysassignment.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val movieDetailRepository: MovieDetailRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    private val _movieListFLow = MutableStateFlow<NetworkState<List<Movie>>>(NetworkState.Loading())
    val movieListFLow: StateFlow<NetworkState<List<Movie>>>
        get() = _movieListFLow.asStateFlow()

    private val _movieDetailsFLow = MutableStateFlow<NetworkState<Movie>>(NetworkState.Loading())
    val movieDetailsFLow: StateFlow<NetworkState<Movie>>
        get() = _movieDetailsFLow.asStateFlow()

    init {
        getMovieList()
    }

    private fun getMovieList() {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                when(val response = movieListRepository.fetchMovieList()) {
                    is NetworkState.Success ->
                        _movieListFLow.value = NetworkState.Success(response.data ?: emptyList())
                    is NetworkState.Error ->
                        _movieListFLow.value = NetworkState.Error(response.message ?: "Error")
                    else -> {}
                }
            }
        } else {
            _movieListFLow.value = NetworkState.Error("No internet available")
        }
    }

    fun getMovieDetails(movieId: String) {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                when(val response = movieDetailRepository.fetchMovieDetails(movieId)) {
                    is NetworkState.Success ->
                        _movieDetailsFLow.value = NetworkState.Success(response.data ?: MovieMapper.defaultMovie())
                    is NetworkState.Error ->
                        _movieDetailsFLow.value = NetworkState.Error(response.message ?: "Error")
                    else -> {}
                }
            }
        } else {
            _movieDetailsFLow.value = NetworkState.Error("No internet available")
        }
    }
}
package com.atlysassignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val networkHelper: NetworkHelper
): ViewModel() {
    private val _movieListFLow = MutableStateFlow<NetworkState<List<Movie>>>(NetworkState.Loading())
    val movieListFLow: StateFlow<NetworkState<List<Movie>>>
        get() = _movieListFLow.asStateFlow()

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
}
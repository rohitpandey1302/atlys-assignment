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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @property movieListRepository Repository for fetching movie lists.
 * @property movieDetailRepository Repository for fetching movie details.
 * @property networkHelper Helper class for checking network connectivity.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val movieDetailRepository: MovieDetailRepository,
    private val networkHelper: NetworkHelper
): ViewModel() {
    private val _movieListFLow = MutableStateFlow<NetworkState<List<Movie>>>(NetworkState.Loading())
    val movieListFLow: StateFlow<NetworkState<List<Movie>>>
        get() = _movieListFLow.asStateFlow()

    val searchQuery = MutableStateFlow("")

    private val _movieDetailsFLow = MutableStateFlow<NetworkState<Movie>>(NetworkState.Loading())
    val movieDetailsFLow: StateFlow<NetworkState<Movie>>
        get() = _movieDetailsFLow.asStateFlow()

    init {
        getMovieList()
        observeSearchQuery()
    }

    /**
     * Fetches the list of movies based on the search query.
     *
     * @param searchQuery The search query to filter movies.
     */
    private fun getMovieList(searchQuery: String = "") {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {

                val response: NetworkState<List<Movie>> = if (searchQuery.isNotEmpty()) {
                    movieListRepository.searchMovie(searchQuery)
                } else {
                    movieListRepository.fetchMovieList()
                }
                when(response) {
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

    /**
     * Fetches the details of a specific movie.
     *
     * @param movieId The ID of the movie to fetch details for.
     */
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

    /**
     * Observes changes in the search query and fetches the movie list accordingly.
     */
    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged() // Only act on new search terms
                .collect { query ->
                    getMovieList(query)
                }
        }
    }
}
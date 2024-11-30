package com.atlysassignment.data.remote

import com.atlysassignment.model.MovieResponseDTO
import retrofit2.Response
import retrofit2.http.GET

private const val TMDB_API_KEY = "e8cf48effc5e091b9795f821e47f8671"

interface MovieApiService {
    @GET("/3/trending/movie/week?language=en-US&api_key=$TMDB_API_KEY")
    suspend fun retrieveMovieList(): Response<MovieResponseDTO>
}
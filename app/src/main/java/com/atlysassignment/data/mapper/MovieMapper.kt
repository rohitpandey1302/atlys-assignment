package com.atlysassignment.data.mapper

import com.atlysassignment.model.Movie
import com.atlysassignment.model.MovieDTO
import com.atlysassignment.model.MovieResponseDTO

object MovieMapper {
    fun mapList(movieResponseDTO: MovieResponseDTO?): List<Movie> {
        return movieResponseDTO?.movieList?.map {
            Movie(
                id = it.id,
                title = it.title,
                originalTitle = it.originalTitle,
                overview = it.overview,
                posterPath = it.posterPath,
            )
        } ?: emptyList()
    }

    fun mapDetails(movieDTO: MovieDTO?): Movie {
        return movieDTO?.let {
            Movie(
                id = movieDTO.id,
                title = movieDTO.title,
                originalTitle = movieDTO.originalTitle,
                overview = movieDTO.overview,
                posterPath = movieDTO.posterPath,
            )
        } ?: defaultMovie()
    }

    fun defaultMovie() = Movie(
        id = 0,
        title = "",
        originalTitle = "",
        overview = "",
        posterPath = ""
    )
}
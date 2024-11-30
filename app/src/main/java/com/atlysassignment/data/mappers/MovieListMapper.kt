package com.atlysassignment.data.mappers

import com.atlysassignment.model.Movie
import com.atlysassignment.model.MovieResponseDTO

object MovieListMapper {
    fun map(body: MovieResponseDTO?): List<Movie> {
        return body?.movieList?.map {
            Movie(
                id = it.id,
                title = it.title,
                originalTitle = it.originalTitle,
                overview = it.overview,
                posterPath = it.posterPath,
            )
        } ?: emptyList()
    }
}
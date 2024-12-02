package com.atlysassignment.model

data class Movie(
    val id: Int,
    val title: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
)

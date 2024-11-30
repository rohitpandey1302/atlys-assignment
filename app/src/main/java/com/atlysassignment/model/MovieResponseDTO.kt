package com.atlysassignment.model

import com.google.gson.annotations.SerializedName

data class MovieResponseDTO(
    @SerializedName("results")
    val movieList: List<MovieDTO>
)

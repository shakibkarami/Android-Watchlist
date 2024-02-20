package com.practice.watchlist.details.presentation

import com.practice.watchlist.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)

package com.practice.watchlist.movieList.data.mappers

import com.practice.watchlist.movieList.data.local.movie.MovieEntity
import com.practice.watchlist.movieList.data.remote.respond.MovieDto
import com.practice.watchlist.movieList.domain.model.Movie

fun MovieDto.toMovieEntity(
    category: String
): MovieEntity{
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: -1,
        category = category,
        genre_ids = try {
            genre_ids?.joinToString { "," } ?: "-1,-2"
        } catch (e: Exception){
            "-1,-2"
        }
    )
}

fun MovieEntity.toMovie(category: String): Movie{
    return Movie(
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,
        id = id,
        category = category,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception){
            listOf(-1, -2)
        }
    )
}
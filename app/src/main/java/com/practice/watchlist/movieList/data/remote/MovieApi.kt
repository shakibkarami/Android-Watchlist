package com.practice.watchlist.movieList.data.remote

import com.practice.watchlist.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://api.themoviedb.org/3/t/p/w500"
        const val API_KEY = "c18be482c0e8ebbe181155b18151e520"
    }
}
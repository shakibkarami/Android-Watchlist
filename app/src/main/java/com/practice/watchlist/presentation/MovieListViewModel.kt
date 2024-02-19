package com.practice.watchlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.watchlist.movieList.domain.model.Movie
import com.practice.watchlist.movieList.domain.repository.MovieListRepository
import com.practice.watchlist.movieList.util.Category
import com.practice.watchlist.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {
    private val _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMoviesList(false)
        getUpcomingMoviesList(false)
    }

    fun onEvent(event: MovieListUiEvent){
        when(event){
            MovieListUiEvent.Navigate -> {
                _movieListState.update { it.copy(
                    isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                ) }
            }
            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.POPULAR){
                    getPopularMoviesList(true)
                } else if (event.category == Category.UPCOMING){
                    getUpcomingMoviesList(true)
                }
            }
        }
    }

    private fun getPopularMoviesList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest {
                    result -> when(result){
                        is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                            }
                        }
                        is Resource.Success -> {
                            result.data?.let { popularList ->
                                _movieListState.update {
                                    it.copy(
                                        popularMovieList = (movieListState.value.popularMovieList + popularList.shuffled()) as List<Movie>,
                                        popularMovieListPage = movieListState.value.popularMovieListPage+1)
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _movieListState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }

                }
            }
        }
    }

    private fun getUpcomingMoviesList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest {
                    result -> when(result){
                is Resource.Error -> {
                    _movieListState.update {
                        it.copy(isLoading = false)
                    }
                }
                is Resource.Success -> {
                    result.data?.let { upcomingList ->
                        _movieListState.update {
                            it.copy(
                                upcomingMovieList = (movieListState.value.upcomingMovieList + upcomingList.shuffled()) as List<Movie>,
                                upcomingMovieListPage = movieListState.value.upcomingMovieListPage+1)
                        }
                    }
                }
                is Resource.Loading -> {
                    _movieListState.update {
                        it.copy(isLoading = result.isLoading)
                    }
                }

            }
            }
        }
    }
}
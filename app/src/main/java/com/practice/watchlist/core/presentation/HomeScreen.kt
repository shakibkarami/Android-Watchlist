package com.practice.watchlist.core.presentation

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.watchlist.movieList.util.Screen
import com.practice.watchlist.presentation.MovieListUiEvent
import com.practice.watchlist.presentation.MovieListViewModel
import com.practice.watchlist.presentation.PopularMovieScreen
import com.practice.watchlist.presentation.components.UpcomingMovieScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            bottomNavigationBar(bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (movieListState.isCurrentPopularScreen)
                            "Popular Movies"
                        else
                            "Upcoming Movies",
                        fontSize = 20.sp
                    )
            }, modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
    )
        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            NavHost(navController = bottomNavController, startDestination = Screen.PopularMovieList.route) {
                composable(Screen.PopularMovieList.route){
                    PopularMovieScreen(navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent)
                }
                composable(Screen.UpcomingMovieList.route){
                    UpcomingMovieScreen(navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent)
                }
                }
        }
    }
}


@Composable
fun bottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit) {

    val items = listOf(
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "Upcoming",
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row (
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
                items.forEachIndexed { index, bottomItem ->  
                    NavigationBarItem(selected = selected.intValue == index,
                        onClick = { selected.intValue = index
                                  when(selected.intValue) {
                                      0 -> {
                                          onEvent(MovieListUiEvent.Navigate)
                                          bottomNavController.popBackStack()
                                          bottomNavController.navigate(Screen.PopularMovieList.route)
                                      }
                                      1 -> {
                                          onEvent(MovieListUiEvent.Navigate)
                                          bottomNavController.popBackStack()
                                          bottomNavController.navigate(Screen.UpcomingMovieList.route)
                                      }


                                  }},
                        icon = { Icon(imageVector = bottomItem.icon, contentDescription = bottomItem.title, tint = MaterialTheme.colorScheme.onBackground) },
                        label = { Text(text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground)})
                }
            }
        }
    }

data class BottomItem(
    val title: String,
    val icon: ImageVector
)
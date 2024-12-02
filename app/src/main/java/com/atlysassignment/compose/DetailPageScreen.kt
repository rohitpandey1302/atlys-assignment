package com.atlysassignment.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.atlysassignment.R
import com.atlysassignment.model.Movie
import com.atlysassignment.ui.viewState.NetworkState
import com.atlysassignment.ui.viewmodels.MainActivityViewModel

private const val TMDB_IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

@Composable
fun DetailPageScreen(
    movieId: String,
    viewModel: MainActivityViewModel,
    navController: NavController
) {
    val state by viewModel.movieDetailsFLow.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    when(state) {
        is NetworkState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        is NetworkState.Error -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) { Text("Error: ${state.message}") }
        is NetworkState.Success -> MovieDetail(state.data, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetail(
    movie: Movie?,
    navController: NavController
) {
    movie?.let {
        Column {
            TopAppBar(
                title = { Text(movie.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = TMDB_IMAGE_PATH + movie.posterPath,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_foreground)
                    ),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(movie.title ?: "", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(movie.overview ?: "", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModelStateFlow

@Composable
fun FavoriteScreen(viewModel: MediaFavouriteViewModelStateFlow, onTrackClick: (TrackData) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val isClickAllowed by viewModel.isClickAllowed.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MediaFavouriteViewModelStateFlow.UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is MediaFavouriteViewModelStateFlow.UiState.Empty -> {
                EmptyFavoritesContent()
            }

            is MediaFavouriteViewModelStateFlow.UiState.Success -> {
                val successState = state
                TrackListContent(
                    tracks = successState.tracks,
                    isClickAllowed = isClickAllowed,
                    onTrackClick = onTrackClick,
                    onDebounceClick = { viewModel.handleTrackClick() }
                )
            }
        }
    }
}
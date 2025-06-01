package com.example.playlistmaker.ui.media.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModelStateFlow

@Composable
fun PlaylistScreen(
    viewModel: MediaPlaylistViewModelStateFlow,
    onPlaylistClick: (Playlist) -> Unit,
    onAddNewPlaylistClick: () -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()
    val playlistStatus by viewModel.playlistStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.showPlaylists()
    }

    MediaPlaylistContent(playlistStatus, playlists, onPlaylistClick, onAddNewPlaylistClick)
}
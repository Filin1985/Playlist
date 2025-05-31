package com.example.playlistmaker.ui.media.playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.ShowPlaylistUseCase
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaPlaylistViewModelStateFlow(private val showPlaylistsUseCase: ShowPlaylistUseCase) : ViewModel() {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    private val _playlistStatus =
        MutableStateFlow<PlaylistStatus<List<Playlist>>>(PlaylistStatus.Empty())
    val playlistStatus: StateFlow<PlaylistStatus<List<Playlist>>> = _playlistStatus.asStateFlow()

    fun showPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            showPlaylistsUseCase.execute().collect { newPlaylists ->
                _playlists.update { newPlaylists }
                _playlistStatus.update {
                    if (newPlaylists.isEmpty()) {
                        PlaylistStatus.Empty()
                    } else {
                        PlaylistStatus.Content(newPlaylists)
                    }
                }
            }
        }
    }
}
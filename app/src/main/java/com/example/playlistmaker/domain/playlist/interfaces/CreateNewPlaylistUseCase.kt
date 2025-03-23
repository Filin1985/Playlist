package com.example.playlistmaker.domain.playlist.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

interface CreateNewPlaylistUseCase {
    suspend fun execute(playlist: Playlist)
}
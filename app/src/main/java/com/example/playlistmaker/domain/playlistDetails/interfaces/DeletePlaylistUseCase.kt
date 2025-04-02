package com.example.playlistmaker.domain.playlistDetails.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

interface DeletePlaylistUseCase {
    suspend fun execute(playlist: Playlist)
}
package com.example.playlistmaker.domain.playlistDetails.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

interface GetPlaylistByIdUseCase {
    suspend fun execute(playlistId: Int): Playlist
}
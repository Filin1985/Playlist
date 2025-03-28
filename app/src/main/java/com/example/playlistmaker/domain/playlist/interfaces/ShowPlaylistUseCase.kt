package com.example.playlistmaker.domain.playlist.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface ShowPlaylistUseCase {
    fun execute(): Flow<List<Playlist>>
}
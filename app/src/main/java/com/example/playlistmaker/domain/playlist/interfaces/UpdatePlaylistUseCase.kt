package com.example.playlistmaker.domain.playlist.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData

interface UpdatePlaylistUseCase {
    suspend fun execute(playlist: Playlist, track: TrackData)
}
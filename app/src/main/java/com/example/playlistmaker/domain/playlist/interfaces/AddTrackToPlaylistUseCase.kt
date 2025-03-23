package com.example.playlistmaker.domain.playlist.interfaces

import com.example.playlistmaker.domain.search.model.TrackData

interface AddTrackToPlaylistUseCase {
    suspend fun execute(track: TrackData)
}
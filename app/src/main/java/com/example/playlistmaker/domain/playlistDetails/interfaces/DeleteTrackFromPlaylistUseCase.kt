package com.example.playlistmaker.domain.playlistDetails.interfaces

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData

interface DeleteTrackFromPlaylistUseCase {
    suspend fun execute(track: TrackData, playlist: Playlist)
}
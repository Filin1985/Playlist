package com.example.playlistmaker.domain.playlistDetails.model

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData

data class PlaylistDetails (
    val playlist: Playlist,
    val tracks: List<TrackData>,
    val durationInMinutes: Int
)
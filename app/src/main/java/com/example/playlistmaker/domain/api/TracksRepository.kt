package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface TracksRepository {
    fun searchTrack(text: String): List<TrackData>
}
package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.Consumer
import com.example.playlistmaker.domain.models.TrackData

interface TracksInteractor {
    fun searchTracks(text: String, consumer: Consumer<List<TrackData>>)
}
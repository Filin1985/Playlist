package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackData

interface TracksInteractor {
    fun searchTrack(text: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<TrackData>)
    }
}
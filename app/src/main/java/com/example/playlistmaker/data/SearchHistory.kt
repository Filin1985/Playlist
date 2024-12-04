package com.example.playlistmaker.data

import com.example.playlistmaker.domain.models.TrackData

interface SearchHistory {
    fun getTrackList(): ArrayList<TrackData>

    fun addTrackToList(track: TrackData)

    fun clearTrackList()
}
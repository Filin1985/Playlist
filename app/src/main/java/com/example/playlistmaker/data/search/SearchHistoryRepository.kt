package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.model.TrackData

interface SearchHistoryRepository {
    fun getTrackList(): ArrayList<TrackData>

    fun addTrackToList(track: TrackData)

    fun clearTrackList()
}
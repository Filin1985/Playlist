package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.ApiResponse
import com.example.playlistmaker.domain.search.model.TrackData

interface TracksRepository {
    fun searchTracks(text: String): ApiResponse<List<TrackData>>
}
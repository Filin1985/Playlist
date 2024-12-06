package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.ApiResponse
import com.example.playlistmaker.domain.search.model.TrackData

interface TracksRepository {
    fun searchTracks(text: String): ApiResponse<List<TrackData>>
}
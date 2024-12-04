package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.domain.models.TrackData

interface TracksRepository {
    fun searchTracks(text: String): ApiResponse<List<TrackData>>
}
package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.models.TrackData

interface NetworkClient {
    fun doRequest(dto: TracksSearchRequest): ApiResponse<List<TrackData>>
}
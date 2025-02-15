package com.example.playlistmaker.data

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface NetworkClient {
    suspend fun doRequest(dto: TracksSearchRequest): Flow<ResponseData<List<TrackData>>>
}
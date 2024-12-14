package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.search.model.ApiResponse
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.TrackData

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(text: String): ApiResponse<List<TrackData>> {
            return networkClient.doRequest(TracksSearchRequest(text))
    }
}

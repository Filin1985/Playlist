package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override suspend fun searchTracks(text: String): Flow<ResponseData<List<TrackData>>> {
            return networkClient.doRequest(TracksSearchRequest(text))
    }
}

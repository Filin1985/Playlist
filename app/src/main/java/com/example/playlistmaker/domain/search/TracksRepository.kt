package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(text: String): Flow<ResponseData<List<TrackData>>>
}
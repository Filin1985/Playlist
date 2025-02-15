package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    suspend fun execute(text: String): Flow<ResponseData<List<TrackData>>>
}
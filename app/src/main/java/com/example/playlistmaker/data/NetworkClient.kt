package com.example.playlistmaker.data

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.domain.search.model.TrackData

interface NetworkClient {
    fun doRequest(dto: TracksSearchRequest): ResponseData<List<TrackData>>
}
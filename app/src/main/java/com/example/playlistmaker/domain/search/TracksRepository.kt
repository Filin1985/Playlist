package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.search.model.TrackData

interface TracksRepository {
    fun searchTracks(text: String): ResponseData<List<TrackData>>
}
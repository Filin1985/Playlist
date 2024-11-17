package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.TrackData

data class TracksResponse(val resultCount: Int, val results: List<TrackData>): Response()


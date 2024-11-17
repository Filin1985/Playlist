package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.TrackData

interface GetTracksHistoryListUseCase {
    fun execute(): ArrayList<TrackData>
}
package com.example.playlistmaker.domain.search.interfaces

import com.example.playlistmaker.domain.search.model.TrackData

interface GetTracksHistoryListUseCase {
    fun execute(): ArrayList<TrackData>
}
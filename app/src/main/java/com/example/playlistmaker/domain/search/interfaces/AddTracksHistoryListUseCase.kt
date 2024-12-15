package com.example.playlistmaker.domain.search.interfaces

import com.example.playlistmaker.domain.search.model.TrackData

interface AddTracksHistoryListUseCase {
    fun execute(track: TrackData)
}
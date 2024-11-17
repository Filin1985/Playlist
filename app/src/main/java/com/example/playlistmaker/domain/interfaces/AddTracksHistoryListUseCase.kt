package com.example.playlistmaker.domain.interfaces

import com.example.playlistmaker.domain.models.TrackData

interface AddTracksHistoryListUseCase {
    fun execute(track: TrackData)
}
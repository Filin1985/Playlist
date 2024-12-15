package com.example.playlistmaker.domain.player.interfaces

import com.example.playlistmaker.domain.search.model.TrackData

interface PreparePlayerUseCase {
    fun execute(track: TrackData,action: () -> Unit)
}
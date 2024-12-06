package com.example.playlistmaker.domain.player.interfaces

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface PreparePlayerUseCase {
    fun execute(action: () -> Unit) : MediaPlayerState
}
package com.example.playlistmaker.domain.player.interfaces

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface PauseTrackUseCase {
    fun execute(action: () -> Unit) : MediaPlayerState
}
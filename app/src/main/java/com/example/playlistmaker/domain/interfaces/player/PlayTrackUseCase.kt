package com.example.playlistmaker.domain.interfaces.player

import com.example.playlistmaker.domain.models.MediaPlayerState

interface PlayTrackUseCase {
    fun execute(action: () -> Unit) : MediaPlayerState
}
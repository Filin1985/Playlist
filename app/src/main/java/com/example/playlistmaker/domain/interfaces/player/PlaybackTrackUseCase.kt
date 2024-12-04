package com.example.playlistmaker.domain.interfaces.player

import com.example.playlistmaker.domain.models.MediaPlayerState

interface PlaybackTrackUseCase {
    fun execute(
        actionPlaying: () -> Unit, actionPause: () -> Unit
    ): MediaPlayerState
}
package com.example.playlistmaker.domain.player.interfaces

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface PlaybackTrackUseCase {
    fun execute(
        actionPlaying: () -> Unit, actionPause: () -> Unit
    ): MediaPlayerState
}
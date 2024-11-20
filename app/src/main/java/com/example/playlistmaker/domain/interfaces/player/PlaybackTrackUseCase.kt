package com.example.playlistmaker.domain.interfaces.player

import android.os.Handler
import android.widget.TextView
import com.example.playlistmaker.domain.models.MediaPlayerState

interface PlaybackTrackUseCase {
    fun execute(
        actionPlaying: () -> Unit, actionPause: () -> Unit
    ): MediaPlayerState
}
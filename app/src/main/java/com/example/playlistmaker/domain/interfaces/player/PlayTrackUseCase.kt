package com.example.playlistmaker.domain.interfaces.player

import android.os.Handler
import android.widget.TextView
import com.example.playlistmaker.domain.models.MediaPlayerState

interface PlayTrackUseCase {
    fun execute(action: () -> Unit) : MediaPlayerState
}
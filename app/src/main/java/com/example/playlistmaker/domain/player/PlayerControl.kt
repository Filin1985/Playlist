package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.MediaPlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerControl {

    fun play()

    fun pause()

    fun getPlayerState(): StateFlow<MediaPlayerState>

    fun getCurrentTime(): Int

    fun showNotification()

    fun hideNotification()

    companion object {
        const val CHANNEL_ID = "player_service"
        const val SERVICE_ID = 100
    }
}
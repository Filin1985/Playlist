package com.example.playlistmaker.data.player

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface MediaPlayerRepository {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer()

    fun getPlayerState(): MediaPlayerState

    fun getCurrentTime(): Int
}
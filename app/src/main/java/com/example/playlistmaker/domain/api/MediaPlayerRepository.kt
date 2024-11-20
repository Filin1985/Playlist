package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.MediaPlayerState

interface MediaPlayerRepository {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer()

    fun getPlayerState(): MediaPlayerState

    fun getCurrentTime(): Int
}
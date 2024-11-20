package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.MediaPlayerState

interface MediaPlayerInteractor {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer(url: String?)

    fun getPlayerState(): MediaPlayerState
}
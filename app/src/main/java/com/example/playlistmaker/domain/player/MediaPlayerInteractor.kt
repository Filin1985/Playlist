package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface MediaPlayerInteractor {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer(url: String?)

    fun getPlayerState(): MediaPlayerState
}
package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.MediaPlayerState

interface MediaPlayerRepository {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer(setPLayerState: () -> Unit)

    fun getPlayerState(): MediaPlayerState

    fun getCurrentTime(): Int

    fun setCompletionState(setPLayerState: () -> Unit)
}
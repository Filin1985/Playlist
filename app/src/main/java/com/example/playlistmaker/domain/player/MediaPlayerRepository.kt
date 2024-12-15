package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

interface MediaPlayerRepository {

    fun play()

    fun pause()

    fun destroy()

    fun preparePlayer(track: TrackData, setPLayerState: () -> Unit)

    fun getPlayerState(): MediaPlayerState

    fun getCurrentTime(): Int

    fun setCompletionState(setPLayerState: () -> Unit)
}
package com.example.playlistmaker.domain.impl.player

import android.util.Log
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.PlayTrackUseCase
import com.example.playlistmaker.domain.models.MediaPlayerState

class PlayTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) : PlayTrackUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.play()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.PauseTrackUseCase
import com.example.playlistmaker.domain.models.MediaPlayerState

class PauseTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PauseTrackUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.pause()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
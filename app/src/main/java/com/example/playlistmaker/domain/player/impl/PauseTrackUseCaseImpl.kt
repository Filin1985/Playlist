package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class PauseTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PauseTrackUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.pause()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
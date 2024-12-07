package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.PreparePlayerUseCase
import com.example.playlistmaker.domain.models.MediaPlayerState

class PreparePlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PreparePlayerUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.preparePlayer()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class PreparePlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PreparePlayerUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.preparePlayer()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
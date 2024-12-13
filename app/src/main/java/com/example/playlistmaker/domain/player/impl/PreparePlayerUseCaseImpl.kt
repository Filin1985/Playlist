package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase

class PreparePlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PreparePlayerUseCase {
    override fun execute(action: () -> Unit) {
        playerRepository.preparePlayer(action)
    }
}
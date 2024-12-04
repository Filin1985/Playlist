package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.DestroyPlayerUseCase

class DestroyPlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    DestroyPlayerUseCase {
    override fun execute() {
        playerRepository.destroy()
    }
}
package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.DestroyPlayerUseCase

class DestroyPlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    DestroyPlayerUseCase {
    override fun execute() {
        playerRepository.destroy()
    }
}
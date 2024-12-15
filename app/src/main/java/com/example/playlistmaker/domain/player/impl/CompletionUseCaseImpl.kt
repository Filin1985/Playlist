package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.CompletionUseCase

class CompletionUseCaseImpl(private val playerRepository: MediaPlayerRepository): CompletionUseCase {
    override fun execute(setPlayerState: () -> Unit) {
        playerRepository.setCompletionState(setPlayerState)
    }
}
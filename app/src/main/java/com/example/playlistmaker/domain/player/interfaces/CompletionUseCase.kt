package com.example.playlistmaker.domain.player.interfaces

interface CompletionUseCase {
    fun execute(setPlayerState: () -> Unit)
}
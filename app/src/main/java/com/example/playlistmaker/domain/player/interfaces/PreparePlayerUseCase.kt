package com.example.playlistmaker.domain.player.interfaces

interface PreparePlayerUseCase {
    fun execute(action: () -> Unit)
}
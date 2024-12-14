package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class PlayTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) : PlayTrackUseCase {
    override fun execute(action: () -> Unit): MediaPlayerState {
        playerRepository.play()
        action.invoke()
        return playerRepository.getPlayerState()
    }
}
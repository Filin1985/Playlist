package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class GetPlayerStateUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    GetPlayerStateUseCase {
    override fun execute(): MediaPlayerState {
        return playerRepository.getPlayerState()
    }
}
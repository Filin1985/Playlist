package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.GetPlayerStateUseCase
import com.example.playlistmaker.domain.models.MediaPlayerState

class GetPlayerStateUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    GetPlayerStateUseCase {
    override fun execute(): MediaPlayerState {
        return playerRepository.getPlayerState()
    }
}
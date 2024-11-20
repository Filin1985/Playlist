package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.GetCurrentPlayerTrackTimeUseCase

class GetCurrentPlayerTrackTimeUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    GetCurrentPlayerTrackTimeUseCase {
    override fun execute(): Int {
        return playerRepository.getCurrentTime()
    }
}
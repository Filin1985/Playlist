package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.GetCurrentPlayerTrackTimeUseCase

class GetCurrentPlayerTrackTimeUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    GetCurrentPlayerTrackTimeUseCase {
    override fun execute(): Int {
        return playerRepository.getCurrentTime()
    }
}
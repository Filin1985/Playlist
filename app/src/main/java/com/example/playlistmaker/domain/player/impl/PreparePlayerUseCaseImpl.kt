package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class PreparePlayerUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PreparePlayerUseCase {
    override fun execute(track: TrackData, action: () -> Unit) {
        playerRepository.preparePlayer(track, action)
    }
}
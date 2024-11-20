package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.models.MediaPlayerState

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository): MediaPlayerInteractor {
    override fun play() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun preparePlayer(url: String?) {
        TODO("Not yet implemented")
    }

    override fun getPlayerState(): MediaPlayerState {
        TODO("Not yet implemented")
    }

}
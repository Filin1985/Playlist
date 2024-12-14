package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.interfaces.PlaybackTrackUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class PlaybackTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PlaybackTrackUseCase {
    override fun execute(actionPlaying: () -> Unit, actionPause: () -> Unit): MediaPlayerState {
        val playerState = playerRepository.getPlayerState()
        return when (playerState) {
            MediaPlayerState.STATE_PLAYING -> PauseTrackUseCaseImpl(playerRepository).execute(
                actionPause
            )

            MediaPlayerState.STATE_PAUSED, MediaPlayerState.STATE_PREPARED -> PlayTrackUseCaseImpl(
                playerRepository
            ).execute(actionPlaying)

            MediaPlayerState.STATE_DEFAULT -> MediaPlayerState.STATE_DEFAULT
        }
    }
}
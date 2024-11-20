package com.example.playlistmaker.domain.impl.player

import android.util.Log
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.interfaces.player.PlayTrackUseCase
import com.example.playlistmaker.domain.interfaces.player.PlaybackTrackUseCase
import com.example.playlistmaker.domain.models.MediaPlayerState

class PlaybackTrackUseCaseImpl(private val playerRepository: MediaPlayerRepository) :
    PlaybackTrackUseCase {
    override fun execute(actionPlaying: () -> Unit, actionPause: () -> Unit): MediaPlayerState {
        val playerState = playerRepository.getPlayerState()
        Log.d("PlaybackTrackUseCaseImpl", "--------------$playerState")
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
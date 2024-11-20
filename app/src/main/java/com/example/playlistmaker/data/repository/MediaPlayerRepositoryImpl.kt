package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.models.MediaPlayerState
import com.example.playlistmaker.domain.models.TrackData

class MediaPlayerRepositoryImpl(val track: TrackData): MediaPlayerRepository {
    companion object {
        private const val DELAY = 500L
        private const val REFRESH_PLAY_TIME = 29900L
    }

    private var playerState = MediaPlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = MediaPlayerState.STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = MediaPlayerState.STATE_PREPARED
            }
        }
    }

    override fun getPlayerState() = playerState
}
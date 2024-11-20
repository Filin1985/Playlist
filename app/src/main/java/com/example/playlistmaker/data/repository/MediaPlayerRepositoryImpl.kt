package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.widget.TextView
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.models.MediaPlayerState
import com.example.playlistmaker.domain.models.TrackData

class MediaPlayerRepositoryImpl(val track: TrackData): MediaPlayerRepository {
    var state = MediaPlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
        state = MediaPlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        state = MediaPlayerState.STATE_PAUSED
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                state = MediaPlayerState.STATE_PREPARED
            }
            setOnCompletionListener {
                state = MediaPlayerState.STATE_PREPARED
            }
        }
    }

    override fun getPlayerState() = state
    override fun getCurrentTime() = mediaPlayer.currentPosition
}
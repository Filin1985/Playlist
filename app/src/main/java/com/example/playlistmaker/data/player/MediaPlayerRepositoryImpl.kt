package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer?) : MediaPlayerRepository {
    private var state = MediaPlayerState.STATE_DEFAULT

    override fun play() {
        mediaPlayer?.start()
        state = MediaPlayerState.STATE_PLAYING
    }

    override fun pause() {
        if (state == MediaPlayerState.STATE_PLAYING) {
            mediaPlayer?.pause()
            state = MediaPlayerState.STATE_PAUSED
        }
    }

    override fun destroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        state = MediaPlayerState.STATE_DEFAULT
    }

    override fun preparePlayer(track: TrackData, setPLayerState: () -> Unit) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                setPLayerState.invoke()
                state = MediaPlayerState.STATE_PREPARED
            }
        }
    }

    override fun setCompletionState(setPLayerState: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            setPLayerState.invoke()
            state = MediaPlayerState.STATE_PREPARED
        }
    }

    override fun getPlayerState() = state
    override fun getCurrentTime() = if (state == MediaPlayerState.STATE_DEFAULT) {
        INITIAL_PLAY_TRACK_TIME_MILLIS
    } else {
        mediaPlayer!!.currentPosition
    }

    companion object {
        private const val INITIAL_PLAY_TRACK_TIME_MILLIS = 0
    }
}
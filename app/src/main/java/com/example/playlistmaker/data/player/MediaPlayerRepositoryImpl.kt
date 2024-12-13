package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

class MediaPlayerRepositoryImpl(val track: TrackData) : MediaPlayerRepository {
    private var state = MediaPlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun play() {
        mediaPlayer.start()
        state = MediaPlayerState.STATE_PLAYING
    }

    override fun pause() {
        if (state == MediaPlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            state = MediaPlayerState.STATE_PAUSED
        }
    }

    override fun destroy() {
        mediaPlayer.release()
    }

    override fun preparePlayer(setPLayerState: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                setPLayerState.invoke()
                state = MediaPlayerState.STATE_PREPARED
            }
        }
    }

    override fun setCompletionState(setPLayerState: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            setPLayerState.invoke()
            state = MediaPlayerState.STATE_PREPARED
        }
    }

    override fun getPlayerState() = state
    override fun getCurrentTime() = mediaPlayer.currentPosition
}
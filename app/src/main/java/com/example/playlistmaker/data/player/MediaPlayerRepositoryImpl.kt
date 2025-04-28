package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

class MediaPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : MediaPlayerRepository {
    private var state = MediaPlayerState.STATE_DEFAULT

    override fun play() {
        Log.d("PLAY_MEDIA_PLA_IMPL----", "${state}")
        if (state == MediaPlayerState.STATE_PREPARED || state == MediaPlayerState.STATE_PAUSED) {
            mediaPlayer.start()
            state = MediaPlayerState.STATE_PLAYING
        }
    }

    override fun pause() {
        Log.d("PAUSE_MEDIA_PLA_IMPL----", "${state}")
        if (state == MediaPlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            state = MediaPlayerState.STATE_PAUSED
        }
    }

    override fun destroy() {
        mediaPlayer.reset()
        state = MediaPlayerState.STATE_DEFAULT
    }

    override fun preparePlayer(track: TrackData, setPLayerState: () -> Unit) {
        try {
            mediaPlayer.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    setPLayerState.invoke()
                    state = MediaPlayerState.STATE_PREPARED
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("MediaPlayer", "Error occurred: $what, $extra")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error preparing player: ${e.message}")
        }
    }

    override fun setCompletionState(setPLayerState: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            setPLayerState.invoke()
            state = MediaPlayerState.STATE_PREPARED
            mediaPlayer.seekTo(0)
        }
    }

    override fun getPlayerState() = state
    override fun getCurrentTime() = if (state == MediaPlayerState.STATE_DEFAULT) {
        INITIAL_PLAY_TRACK_TIME_MILLIS
    } else {
        mediaPlayer.currentPosition
    }

    companion object {
        private const val INITIAL_PLAY_TRACK_TIME_MILLIS = 0
    }
}
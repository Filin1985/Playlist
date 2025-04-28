package com.example.playlistmaker.ui.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PlayerService : Service(), PlayerControl {
    private val binder = PlayerServiceBinder()
    private var track: TrackData? = null
    private var player: MediaPlayerRepository =
        MediaPlayerRepositoryImpl(mediaPlayer = MediaPlayer())

    private val _mediaPlayerState = MutableStateFlow(MediaPlayerState.STATE_DEFAULT)
    private val mediaPlayerState: StateFlow<MediaPlayerState>
        get() = _mediaPlayerState

    override fun onBind(intent: Intent?): IBinder {
        if (intent?.getStringExtra(SearchFragment.TRACK) != null) {
            intent.getStringExtra(SearchFragment.TRACK)?.let { jsonString ->
                try {
                    track = Gson().fromJson(jsonString, TrackData::class.java)

                    track?.let {
                        player.preparePlayer(it) {
                            pause()
                        }
                        _mediaPlayerState.value = MediaPlayerState.STATE_PREPARED
                    }
                } catch (e: Exception) {
                    Log.e("TRACK_PARSE", "Error parsing track data", e)
                }
            }
        }
        return binder
    }

    override fun pause() {
        player.pause()
        _mediaPlayerState.value = MediaPlayerState.STATE_PAUSED
    }

    override fun getPlayerState(): StateFlow<MediaPlayerState> {
        return mediaPlayerState
    }

    override fun getCurrentTime(): Int {
        return player.getCurrentTime()
    }

    override fun play() {
        player.play()
        _mediaPlayerState.value = MediaPlayerState.STATE_PLAYING
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        destroy()
        return super.onUnbind(intent)
    }

    private fun destroy() {
        player.destroy()
        _mediaPlayerState.value = MediaPlayerState.STATE_DEFAULT
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }
}
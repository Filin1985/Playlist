package com.example.playlistmaker.ui.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
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
            val newTrackJson = intent.getStringExtra(SearchFragment.TRACK)
            val newTrack = Gson().fromJson(newTrackJson, TrackData::class.java)

            if (track == null || track?.trackId != newTrack.trackId) {
                track = newTrack
                player.preparePlayer(newTrack) {
                    pause()
                    player.setCompletionState {
                        _mediaPlayerState.value = MediaPlayerState.STATE_PREPARED
                        hideNotification()
                    }
                }
                _mediaPlayerState.value = MediaPlayerState.STATE_PREPARED
            }
        }
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel = NotificationChannel(
            PlayerControl.CHANNEL_ID,
            this.resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_MIN
        )
        channel.description = "Media player service"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, PlayerControl.CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("${track?.artistName} - ${track?.trackName}")
            .setSmallIcon(R.drawable.ic_media)
            .setLargeIcon(getDrawable(R.drawable.ic_media)?.toBitmap())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            PlayerControl.SERVICE_ID,
            createNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }
}
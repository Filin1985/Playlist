package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.interfaces.CompletionUseCase
import com.example.playlistmaker.domain.player.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.player.interfaces.GetCurrentPlayerTrackTimeUseCase
import com.example.playlistmaker.domain.player.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlaybackTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

class PlayerVewModel(
    private val track: TrackData,
    private val preparePlayer: PreparePlayerUseCase,
    private val destroyPlayer: DestroyPlayerUseCase,
    private val pausePlayer: PauseTrackUseCase,
    private val playbackPlayer: PlaybackTrackUseCase,
    private val playTrackPlayer: PlayTrackUseCase,
    private val getPlayerTime: GetCurrentPlayerTrackTimeUseCase,
    private val getPlayerState: GetPlayerStateUseCase,
    private val setCompletionPlayer: CompletionUseCase
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val stateMutableLiveData = MutableLiveData<MediaPlayerState>().also {
        it.value = getPlayerState.execute()
    }
    val stateLiveData: LiveData<MediaPlayerState> = stateMutableLiveData

    private val playTrackProgressMutableLiveData = MutableLiveData<Int>().also {
        it.value = getPlayerTime.execute()
    }
    val playTrackProgressLiveData: LiveData<Int> = playTrackProgressMutableLiveData

    init {
        preparePlayer.execute(track) {
            stateMutableLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        }
        setCompletionPlayer.execute {
            stateMutableLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        }
    }

    fun pausePlayer() {
        stateMutableLiveData.postValue(pausePlayer.execute { })
    }

    fun playControl() {
        Log.d("PLAY_CONTROL", "$track")
        stateMutableLiveData.postValue(playbackPlayer.execute(
            actionPause = { }, actionPlaying = { }
        ))
    }

    private fun createUpdaterRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                playTrackProgressMutableLiveData.value = getPlayerTime.execute()
                val playerState = getPlayerState.execute()
                when (playerState) {
                    MediaPlayerState.STATE_PLAYING -> handler.postDelayed(this, DELAY_MILLIS)
                    MediaPlayerState.STATE_PAUSED -> handler.removeCallbacks(this)
                    MediaPlayerState.STATE_PREPARED -> handler.removeCallbacks(this)
                    MediaPlayerState.STATE_DEFAULT -> {}
                }
            }
        }
    }

    fun startUpdaterRunnable() {
        handler.post(createUpdaterRunnable())
    }

    override fun onCleared() {
        destroyPlayer.execute()
        super.onCleared()
    }

    companion object {
        private const val DELAY_MILLIS = 500L
    }
}
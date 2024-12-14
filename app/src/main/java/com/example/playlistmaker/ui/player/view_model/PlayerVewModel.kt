package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData

class PlayerVewModel(track: TrackData): ViewModel() {
    private val trackCreator by lazy { Creator.getPlayerCreator(track) }

    private val preparePlayer by lazy { trackCreator.providePreparePlayerUseCase() }
    private val destroyPlayer by lazy { trackCreator.provideDestroyPlayerUseCase() }
    private val pausePlayer by lazy { trackCreator.providePauseTrackUseCase() }
    private val playbackPlayer by lazy { trackCreator.providePlaybackTrackUseCase() }
    private val playTrackPlayer by lazy { trackCreator.providePlayTrackUseCase() }
    private val getPlayerTime by lazy { trackCreator.providePlayTrackTimeUseCase() }
    private val getPlayerState by lazy { trackCreator.provideGetPlayerStateUseCase() }
    private val setCompletionPlayer by lazy { trackCreator.provideCompletionUseCase() }

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
        preparePlayer.execute {
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

        fun factory(track: TrackData): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerVewModel(track)
                }
            }
        }
    }
}
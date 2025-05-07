package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.search.mappers.TrackListMapper
import com.example.playlistmaker.domain.favorites.interfaces.DeleteFavoriteTrackUseCase
import com.example.playlistmaker.domain.favorites.interfaces.GetFavoriteTracksIdsUseCase
import com.example.playlistmaker.domain.favorites.interfaces.InsertFavoriteTrackUseCase
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.interfaces.CompletionUseCase
import com.example.playlistmaker.domain.player.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.player.interfaces.GetCurrentPlayerTrackTimeUseCase
import com.example.playlistmaker.domain.player.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlaybackTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.player.model.TrackPlaylistState
import com.example.playlistmaker.domain.playlist.interfaces.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.playlist.interfaces.ShowPlaylistUseCase
import com.example.playlistmaker.domain.playlist.interfaces.UpdatePlaylistUseCase
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import com.example.playlistmaker.domain.search.model.TrackData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerVewModel(
    private val track: String,
    private val getPlayerTime: GetCurrentPlayerTrackTimeUseCase,
    private val getPlayerState: GetPlayerStateUseCase,
    private val setCompletionPlayer: CompletionUseCase,
    private val deleteTrackFromFavorite: DeleteFavoriteTrackUseCase,
    private val insertTrackToFavorite: InsertFavoriteTrackUseCase,
    private val getTrackIdsFromDb: GetFavoriteTracksIdsUseCase,
    private val showPlaylistsUseCase: ShowPlaylistUseCase,
    private val addTrackToPlaylistUseCase: AddTrackToPlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase
) : ViewModel() {

    private val _playerState = MutableStateFlow(MediaPlayerState.STATE_DEFAULT)
    val playerState: StateFlow<MediaPlayerState> get() = _playerState
    val trackData = Gson().fromJson(track, TrackData::class.java)

    private val playTrackProgressMutableLiveData = MutableLiveData<Int>().also {
        it.value = getPlayerTime.execute()
    }
    val playTrackProgressLiveData: LiveData<Int> = playTrackProgressMutableLiveData

    private val isTrackInFavoriteMutableData = MutableLiveData<Boolean>()
    val isTrackInFavoriteLiveData: LiveData<Boolean> = isTrackInFavoriteMutableData

    private var timerJob: Job? = null
    private val _timer = MutableLiveData(CURRENT_TIME)
    val timeProgress: LiveData<String>
        get() = _timer

    private val playlists = mutableListOf<Playlist>()

    private val playlistMutableLiveData = MutableLiveData<List<Playlist>>(playlists)
    val playlistLiveData: LiveData<List<Playlist>> = playlistMutableLiveData

    private val playlistStateMutableLiveData =
        MutableLiveData<TrackPlaylistState>(TrackPlaylistState.TRACK_IS_ADDED_UNKNOWN)
    val playlistStateLiveData: LiveData<TrackPlaylistState> = playlistStateMutableLiveData

    private var playerControl: PlayerControl? = null

    init {
        setCompletionPlayer.execute {
            _playerState.value = MediaPlayerState.STATE_PREPARED
        }
        setIsTrackInFavorite()
    }

    fun playerControlManager(playerControl: PlayerControl) {
        this.playerControl = playerControl
        viewModelScope.launch {
            playerControl.getPlayerState().collect {
                    _playerState.value = it
            }
        }
    }


    fun pausePlayer() {
        timerJob?.cancel()
        playerControl?.pause()
    }

    fun startPlayer() {
        playerControl?.play()
    }

    fun playbackControl() {
        when (playerState.value) {
            MediaPlayerState.STATE_PLAYING -> pausePlayer()
            MediaPlayerState.STATE_PREPARED, MediaPlayerState.STATE_PAUSED -> startPlayer()
            else -> Unit
        }
        updateTimer()
    }

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (getPlayerState.execute() == MediaPlayerState.STATE_PLAYING) {
                delay(DELAY_MILLIS)
                playTrackProgressMutableLiveData.value = getPlayerTime.execute()
            }
        }
    }

    private fun updateTimer() {
        timerJob?.cancel()
        when (playerState.value) {
            MediaPlayerState.STATE_PLAYING -> {
                timerJob = viewModelScope.launch {
                    while (true) {
                        _timer.value = getTimerPosition()
                        delay(DELAY_MILLIS)
                    }
                }
            }

            MediaPlayerState.STATE_PAUSED -> timerJob?.cancel()
            else -> _timer.value = CURRENT_TIME
        }
    }

    private fun getTimerPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerControl?.getCurrentTime())
    }

    fun toggleTrackFavoriteState() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isTrackInFavoriteMutableData.value == true) {
                isTrackInFavoriteMutableData.postValue(false)
                deleteTrackFromFavorite.execute(trackData)
            } else {
                isTrackInFavoriteMutableData.postValue(true)
                insertTrackToFavorite.execute(trackData)
            }
        }
    }

    private fun setIsTrackInFavorite() {
        val trackFromDb = mutableListOf<String>()

        viewModelScope.async(Dispatchers.IO) {
            getTrackIdsFromDb.execute().collect {
                trackFromDb.addAll(it)
            }
            val isTrackInFavorite = trackFromDb.contains(trackData.trackId)
            isTrackInFavoriteMutableData.postValue(isTrackInFavorite)
        }
    }

    fun showPlaylists() {
        playlists.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showPlaylistsUseCase.execute().collect {
                playlists.addAll(it)
                if (playlists.isNotEmpty()) {
                    playlistMutableLiveData.postValue(playlists)
                }
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (playlist.tracksId.contains(trackData.trackId)) {
            playlistStateMutableLiveData.value = TrackPlaylistState.TRACK_IS_ALREADY_ADDED
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                addTrackToPlaylistUseCase.execute(track = trackData)
                updatePlaylistUseCase.execute(
                    playlist = playlist,
                    track = trackData
                )
            }
            playlistStateMutableLiveData.postValue(TrackPlaylistState.TRACK_IS_ADDED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        playerControl = null
    }

    fun removePlayerControl() {
        playerControl = null
    }

    fun showNotification() {
        playerControl?.showNotification()
    }

    fun hideNotification() {
        playerControl?.hideNotification()
    }

    companion object {
        private const val DELAY_MILLIS = 150L
        private const val CURRENT_TIME = "00:00"
    }
}

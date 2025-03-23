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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerVewModel(
    private val track: TrackData,
    private val preparePlayer: PreparePlayerUseCase,
    private val destroyPlayer: DestroyPlayerUseCase,
    private val pausePlayer: PauseTrackUseCase,
    private val playbackPlayer: PlaybackTrackUseCase,
    private val playTrackPlayer: PlayTrackUseCase,
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

    private val stateMutableLiveData = MutableLiveData<MediaPlayerState>().also {
        it.value = getPlayerState.execute()
    }
    val stateLiveData: LiveData<MediaPlayerState> = stateMutableLiveData

    private val playTrackProgressMutableLiveData = MutableLiveData<Int>().also {
        it.value = getPlayerTime.execute()
    }
    val playTrackProgressLiveData: LiveData<Int> = playTrackProgressMutableLiveData

    private val isTrackInFavoriteMutableData = MutableLiveData<Boolean>()
    val isTrackInFavoriteLiveData: LiveData<Boolean> = isTrackInFavoriteMutableData

    private var timerJob: Job? = null

    private val playlists = mutableListOf<Playlist>()

    private val playlistMutableLiveData = MutableLiveData<List<Playlist>>(playlists)
    val playlistLiveData: LiveData<List<Playlist>> = playlistMutableLiveData

    private val playlistStateMutableLiveData = MutableLiveData<TrackPlaylistState>(TrackPlaylistState.TRACK_IS_ADDED_UNKNOWN)
    val playlistStateLiveData: LiveData<TrackPlaylistState> = playlistStateMutableLiveData

    init {
        preparePlayer.execute(track) {
            stateMutableLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        }
        setCompletionPlayer.execute {
            stateMutableLiveData.postValue(MediaPlayerState.STATE_PREPARED)
        }

        setIsTrackInFavorite()
    }

    fun pausePlayer() {
        timerJob?.cancel()
        stateMutableLiveData.postValue(pausePlayer.execute { })
    }

    fun playControl() {
        Log.d("PLAY_CONTROL", "$track")
        stateMutableLiveData.postValue(playbackPlayer.execute(
            actionPause = { }, actionPlaying = { }
        ))
    }

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (getPlayerState.execute() == MediaPlayerState.STATE_PLAYING) {
                delay(DELAY_MILLIS)
                playTrackProgressMutableLiveData.value = getPlayerTime.execute()
            }
        }
    }

    fun toggleTrackFavoriteState() {
        viewModelScope.launch(Dispatchers.IO) {
            if(isTrackInFavoriteMutableData.value == true) {
                isTrackInFavoriteMutableData.postValue(false)
                deleteTrackFromFavorite.execute(track)
            } else {
                isTrackInFavoriteMutableData.postValue(true)
                insertTrackToFavorite.execute(track)
            }
        }
    }

    private fun setIsTrackInFavorite() {
        val trackFromDb = mutableListOf<String>()

        viewModelScope.async(Dispatchers.IO) {
            getTrackIdsFromDb.execute().collect {
                trackFromDb.addAll(it)
            }
            val isTrackInFavorite = trackFromDb.contains(track.trackId)
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
        if (playlist.tracksId.contains(track.trackId)) {
            playlistStateMutableLiveData.value = TrackPlaylistState.TRACK_IS_ALREADY_ADDED
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                addTrackToPlaylistUseCase.execute(track = track)
                updatePlaylistUseCase.execute(
                    playlist = playlist,
                    track = track
                )
            }
            playlistStateMutableLiveData.postValue(TrackPlaylistState.TRACK_IS_ADDED)
        }
    }

    override fun onCleared() {
        destroyPlayer.execute()
        super.onCleared()
    }

    companion object {
        private const val DELAY_MILLIS = 300L
    }
}
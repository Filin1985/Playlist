package com.example.playlistmaker.ui.media.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.ShowPlaylistUseCase
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPlaylistViewModel(private val showPlaylistsUseCase: ShowPlaylistUseCase): ViewModel() {
    private val playlists = mutableListOf<Playlist>()

    private val playlistMutableLiveData = MutableLiveData<List<Playlist>>(playlists)
    val playlistLiveData: LiveData<List<Playlist>> = playlistMutableLiveData

    private val playlistStatusMutableLiveData = MutableLiveData<PlaylistStatus<List<Playlist>>>()
    val playlistStatusLiveData: LiveData<PlaylistStatus<List<Playlist>>> = playlistStatusMutableLiveData

    fun showPlaylists() {
        playlists.clear()
        viewModelScope.launch(Dispatchers.IO) {
            showPlaylistsUseCase.execute().collect {
                playlists.addAll(it)
                if(playlists.isEmpty()) {
                    playlistStatusMutableLiveData.postValue(PlaylistStatus.Empty())
                } else {
                    playlistMutableLiveData.postValue(playlists)
                    playlistStatusMutableLiveData.postValue(PlaylistStatus.Content(playlists))
                }
            }
        }
    }
}
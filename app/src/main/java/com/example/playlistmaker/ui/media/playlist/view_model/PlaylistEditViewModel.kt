package com.example.playlistmaker.ui.media.playlist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.UpdatePlaylistUseCase
import com.example.playlistmaker.domain.playlistDetails.interfaces.GetPlaylistByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditViewModel(private val playlistId: Int,
                            private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
                            private val updatePlaylistUseCase: UpdatePlaylistUseCase,
): ViewModel() {

    private val playlistDataMutableLive = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> get() = playlistDataMutableLive

    private val dataPlaylistBeforeEditingMutableLive = MutableLiveData<Playlist>()
    val playlistBeforeEditingLiveData: MutableLiveData<Playlist> get() = dataPlaylistBeforeEditingMutableLive

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistByIdUseCase.execute(playlistId).also {
                playlistDataMutableLive.postValue(it)
                dataPlaylistBeforeEditingMutableLive.postValue(it)
            }
        }
    }

    fun updatePlaylist() {
        if (playlistLiveData.value != playlistBeforeEditingLiveData.value) {
            viewModelScope.launch(Dispatchers.IO) {
                updatePlaylistUseCase.execute(playlistLiveData.value!!)
            }
        }
    }

    fun setNewPlaylistTitle(newTitle: String) {
        val playlistWithUpdatedTitle = playlistLiveData.value!!.copy(
            title = newTitle
        )
        playlistDataMutableLive.value = playlistWithUpdatedTitle
    }

    fun setNewPlaylistDescription(newDescription: String) {
        val playlistWithUpdatedDescription = playlistLiveData.value!!.copy(
            description = newDescription
        )
        playlistDataMutableLive.value = playlistWithUpdatedDescription
    }

    fun setNewPlaylistUri(newUri: Uri) {
        val playlistWithUpdatedUri = playlistLiveData.value!!.copy(
            coverUri = newUri
        )
        playlistDataMutableLive.value = playlistWithUpdatedUri
    }
}
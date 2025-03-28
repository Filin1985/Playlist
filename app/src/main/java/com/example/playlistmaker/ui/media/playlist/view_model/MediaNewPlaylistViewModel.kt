package com.example.playlistmaker.ui.media.playlist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.CreateNewPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaNewPlaylistViewModel(private val createNewPlaylistUseCase: CreateNewPlaylistUseCase) :
    ViewModel() {
    private val playlistUriMutableLiveData = MutableLiveData<Uri?>()
    val playlistUriLiveData: LiveData<Uri?> = playlistUriMutableLiveData

    fun createNewPlaylist(
        title: String,
        description: String? = null,
        uri: Uri? = playlistUriLiveData.value
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            createNewPlaylistUseCase.execute(
                Playlist(
                    title = title,
                    description = description,
                    coverUri = uri
                )
            )
        }
    }

    fun setUri(uri: Uri) {
        playlistUriMutableLiveData.value = uri
    }
}
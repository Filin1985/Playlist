package com.example.playlistmaker.ui.media.playlist.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaNewPlaylistViewModel: ViewModel() {
    private val playlistUriMutableLiveData = MutableLiveData<Uri?>()
    val playlistUriLiveData: LiveData<Uri?> = playlistUriMutableLiveData

    fun setUri(uri: Uri) {
        playlistUriMutableLiveData.value = uri
    }
}
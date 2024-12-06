package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.model.MediaPlayerState

class PlayerVewModel: ViewModel() {
    private val state = MutableLiveData<MediaPlayerState>()

    fun getState(): LiveData<MediaPlayerState> = state


}
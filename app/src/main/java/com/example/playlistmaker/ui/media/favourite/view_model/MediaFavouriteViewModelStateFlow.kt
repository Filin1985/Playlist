package com.example.playlistmaker.ui.media.favourite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.interfaces.GetFavoriteTracksUseCase
import com.example.playlistmaker.domain.favorites.model.FavoriteTracksState
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaFavouriteViewModelStateFlow(
    private val getFavoriteTracks: GetFavoriteTracksUseCase
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        object Empty : UiState()
        data class Success(val tracks: List<TrackData>) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _isClickAllowed = MutableStateFlow(true)
    val isClickAllowed: StateFlow<Boolean> = _isClickAllowed

    init {
        loadFavoriteTracks()
    }

    fun loadFavoriteTracks() {
        viewModelScope.launch(Dispatchers.IO) {  // ← Run on IO dispatcher
            _uiState.value = UiState.Loading
            getFavoriteTracks.execute()
                .collect { tracks ->
                    withContext(Dispatchers.Main) {
                        _uiState.value = if (tracks.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(tracks)
                        }
                    }
                }
        }
    }

    fun handleTrackClick() {
        if (!_isClickAllowed.value) return

        _isClickAllowed.value = false
        viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            _isClickAllowed.value = true
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

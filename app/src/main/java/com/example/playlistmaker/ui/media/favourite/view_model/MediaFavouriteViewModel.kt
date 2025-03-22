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
import kotlinx.coroutines.launch

class MediaFavouriteViewModel(
    private val getFavoriteTracks: GetFavoriteTracksUseCase,
) : ViewModel() {

    private val favoriteTracks = mutableListOf<TrackData>()

    private val favoritesTrackListMutableLiveData = MutableLiveData(favoriteTracks)
    val favoritesTrackListLiveData: LiveData<MutableList<TrackData>> = favoritesTrackListMutableLiveData

    private val favoriteTrackStateMutableData = MutableLiveData<FavoriteTracksState>()
    val favoriteTrackStateLiveData: LiveData<FavoriteTracksState> = favoriteTrackStateMutableData

    private val isClickAllowedMutableLiveData = MutableLiveData(true)
    val isClickAllowedLiveData: LiveData<Boolean> = isClickAllowedMutableLiveData

    init {
        if (favoriteTracks.isNotEmpty()) favoriteTrackStateMutableData.value = FavoriteTracksState.FAVORITE_LIST
    }

    fun showFavoriteTracks() {
        favoriteTracks.clear()
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteTracks.execute().collect {
                favoriteTracks.addAll(it)
            }

            if(favoriteTracks.isEmpty()) {
                favoriteTrackStateMutableData.postValue(FavoriteTracksState.EMPTY_FAVORITE_DATA)
            } else {
                favoriteTrackStateMutableData.postValue(FavoriteTracksState.FAVORITE_LIST)
                favoritesTrackListMutableLiveData.postValue(favoriteTracks)
            }
        }
    }

    fun clickOnTrackDebounce(track: TrackData) {
        if (isClickAllowedLiveData.value!!) {
            isClickAllowedMutableLiveData.value = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowedMutableLiveData.value = true
            }
        }
    }

    fun getFavoriteTrackStateLiveData() {
        return
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
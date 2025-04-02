package com.example.playlistmaker.ui.playlistDetails.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlistDetails.interfaces.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.domain.playlistDetails.interfaces.GetPlaylistByIdUseCase
import com.example.playlistmaker.domain.playlistDetails.interfaces.GetTracksFromPlaylistUseCase
import com.example.playlistmaker.domain.playlistDetails.model.PlaylistDetails
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.mapper.TrackMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DetailPlaylistViewModel(
    private val playlistId: Int,
    private val getPlaylistByIdUseCase: GetPlaylistByIdUseCase,
    private val getTracksFromPlaylistUseCase: GetTracksFromPlaylistUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase
) : ViewModel() {

    private val playlistDataMutableLive = MutableLiveData<PlaylistDetails>()
    val playlistLiveData: LiveData<PlaylistDetails> = playlistDataMutableLive

    private val dataIsClickOnTrackAllowedMutableLive = MutableLiveData<Boolean>(true)
    val isClickOnTrackAllowedLiveData: LiveData<Boolean> = dataIsClickOnTrackAllowedMutableLive


    fun getPlaylistInfo() {
        viewModelScope.launch(Dispatchers.IO) {

            val playlist = getPlaylistByIdUseCase.execute(playlistId)

            getTracksFromPlaylistUseCase.execute(playlist.tracksId).collect {
                val tracks = it.map{ TrackMapper.mapTrackToTrackRepresentation(it) }
                Log.d("TRACKS------------", "${tracks}")
                val playlistDurationMillis = it.sumOf { it.trackTimeMillis }
                val playlistDuration = convertMillisToMinutes(playlistDurationMillis)

                playlistDataMutableLive.postValue(
                    PlaylistDetails(
                        playlist = playlist,
                        tracks = tracks,
                        durationInMinutes = playlistDuration
                    )
                )
            }
        }
    }

    private fun convertMillisToMinutes(millis: Int): Int {
        return TimeUnit.MILLISECONDS.toMinutes(millis.toLong()).toInt()
    }

    fun clickOnTrackDebounce() {
        if (isClickOnTrackAllowedLiveData.value!!) {
            dataIsClickOnTrackAllowedMutableLive.value = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                dataIsClickOnTrackAllowedMutableLive.value = true
            }
        }
    }

    fun deleteTrack(track: TrackData) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTrackFromPlaylistUseCase.execute(
                track = track,
                playlist = playlistLiveData.value?.playlist!!
            )
            getPlaylistInfo()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
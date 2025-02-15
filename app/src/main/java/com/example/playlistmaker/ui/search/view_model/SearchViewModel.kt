package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections

class SearchViewModel(
    private val getHistoryTrackListToStorageUseCase: GetTracksHistoryListUseCase,
    private val searchTrackListUseCase: TracksInteractor,
    private val addTrackToHistoryUseCase: AddTracksHistoryListUseCase,
    private val clearTrackHistoryUseCase: ClearTracksHistoryListUseCase
) : ViewModel() {

    private val searchTrackList = mutableListOf<TrackData>()
    private val searchTrackListMutableData = MutableLiveData(searchTrackList)
    val searchTrackListLiveData: LiveData<MutableList<TrackData>> = searchTrackListMutableData

    private val searchTrackState = MutableLiveData<SearchState>()
    val liveDataState: LiveData<SearchState> = searchTrackState

    private val searchHistoryTrackList =
        getHistoryTrackListToStorageUseCase.execute()
    private val historyTrackListMutableData =
        MutableLiveData<MutableList<TrackData>>(searchHistoryTrackList)
    val searchHistoryTrackListLiveData: LiveData<MutableList<TrackData>> =
        historyTrackListMutableData

    private val searchRequestMutableData = MutableLiveData<String>()
    val searchRequestLiveData: LiveData<String> = searchRequestMutableData

    private val isClickAllowedMutableLiveData = MutableLiveData(true)
    val isClickAllowedLiveData: LiveData<Boolean> = isClickAllowedMutableLiveData

    private var searchJob: Job? = null

    init {
        if (searchHistoryTrackList.isNotEmpty()) searchTrackState.value = SearchState.HISTORY_LIST
    }

    private fun searchTrackList(data: ResponseData<List<TrackData>>) {
        when (data) {
            is ResponseData.Success -> {
                searchTrackList.clear()
                searchTrackList.addAll(data.data)
                searchTrackState.value = SearchState.SUCCESS
            }

            is ResponseData.EmptyResponse -> {
                searchTrackList.clear()
                searchTrackState.value = SearchState.NOT_FOUND
            }

            is ResponseData.Error -> {
                searchTrackList.clear()
                searchTrackState.value = SearchState.CONNECTION_ERROR
            }
        }
    }

    private fun startTrackSearch(searchRequest: String) {
        if (searchRequest.isNotEmpty()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                searchTrackListUseCase.execute(searchRequest).collect {
                    searchTrackList(it)
                }
            }
        }
    }

    fun startImmediateSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            searchJob?.cancel()
            startTrackSearch(searchRequest)
        }
        searchRequestMutableData.value = searchRequest
    }

    fun startDebounceSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                startTrackSearch(searchRequest)
            }
            searchTrackState.value = SearchState.SEARCH_PROGRESS
        }
        searchRequestMutableData.value = searchRequest
    }

    fun clickOnTrackDebounce(track: TrackData) {
        if (isClickAllowedLiveData.value!!) {
            isClickAllowedMutableLiveData.value = false
            saveHistoryToStorage(track)
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowedMutableLiveData.value = true
            }
        }
    }

    fun saveHistoryToStorage(track: TrackData) {
        addTrackToHistoryUseCase.execute(track)
    }

    fun writeTrackToList(track: TrackData) {
        val trackIndex = searchHistoryTrackList.indexOfFirst { it.trackId == track.trackId }
        if (trackIndex != -1) {
            Collections.swap(searchHistoryTrackList, trackIndex, searchHistoryTrackList.size - 1)
        } else {
            if (searchHistoryTrackList.size == HISTORY_TRACK_LIST_SIZE) {
                searchHistoryTrackList.removeAt(0)
            }
            searchHistoryTrackList.add(track)
        }
    }

    fun clearHistory() {
        searchHistoryTrackList.clear()
        searchHistoryTrackListLiveData.value!!.clear()
        clearTrackHistoryUseCase.execute()
    }

    fun clear() {
        searchTrackList.clear()
        searchTrackState.value = SearchState.HISTORY_LIST
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val HISTORY_TRACK_LIST_SIZE = 10
    }
}
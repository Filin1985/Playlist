package com.example.playlistmaker.ui.search.view_model

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Collections

class SearchViewModel(
    private val getHistoryTrackListToStorageUseCase: GetTracksHistoryListUseCase,
    private val searchTrackListUseCase: TracksInteractor,
    private val addTrackToHistoryUseCase: AddTracksHistoryListUseCase,
    private val clearTrackHistoryUseCase: ClearTracksHistoryListUseCase
) : ViewModel() {

    private val _searchTrackList = MutableStateFlow<List<TrackData>>(emptyList())
    val searchTrackListLiveData: StateFlow<List<TrackData>> = _searchTrackList

    private val searchTrackState = MutableStateFlow<SearchState>(SearchState.HISTORY_LIST)
    val liveDataState: StateFlow<SearchState> = searchTrackState

    private val searchHistoryTrackList =
        getHistoryTrackListToStorageUseCase.execute()
    private val historyTrackListMutableData =
        MutableStateFlow<MutableList<TrackData>>(searchHistoryTrackList)
    val searchHistoryTrackListLiveData: StateFlow<MutableList<TrackData>> =
        historyTrackListMutableData

    private val _searchRequestMutableData = MutableStateFlow("")
    val searchRequestLiveData: StateFlow<String> = _searchRequestMutableData.asStateFlow()

    private val isClickAllowedMutableLiveData = MutableStateFlow(true)
    val isClickAllowedLiveData: StateFlow<Boolean> = isClickAllowedMutableLiveData

    private var searchJob: Job? = null
    private var lastClickTime = 0L

    init {
        searchTrackState.value = if (searchHistoryTrackList.isNotEmpty()) {
            SearchState.HISTORY_LIST
        } else {
            SearchState.EMPTY_DATA
        }
    }

    fun searchTrackList(data: ResponseData<List<TrackData>>) {
        when (data) {
            is ResponseData.Success -> {
                _searchTrackList.value = emptyList()
                _searchTrackList.value = data.data
                searchTrackState.value = SearchState.SUCCESS
            }

            is ResponseData.EmptyResponse -> {
                _searchTrackList.value = emptyList()
                searchTrackState.value = SearchState.NOT_FOUND
            }

            is ResponseData.Error -> {
                _searchTrackList.value = emptyList()
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
            _searchTrackList.value = emptyList()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            searchJob?.cancel()
            searchTrackState.value = SearchState.SEARCH_PROGRESS
            startTrackSearch(searchRequest)
        }
        _searchRequestMutableData.value = searchRequest
    }

    fun startDebounceSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            _searchTrackList.value = emptyList()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                startTrackSearch(searchRequest)
            }
            searchTrackState.value = SearchState.SEARCH_PROGRESS
        }
        _searchRequestMutableData.value = searchRequest
    }

    fun clickOnTrackDebounce(track: TrackData) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= CLICK_DEBOUNCE_DELAY) {
            lastClickTime = currentTime
            writeTrackToList(track)
            saveHistoryToStorage(track)
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
        historyTrackListMutableData.value = searchHistoryTrackList.toMutableList()
        updateSearchState()
    }

    fun onSearchRequestChange(text: String) {
        _searchRequestMutableData.value = text
    }

    fun clearHistory() {
        searchHistoryTrackList.clear()
        historyTrackListMutableData.value = mutableListOf()
        clearTrackHistoryUseCase.execute()
        updateSearchState()
    }

    private fun updateSearchState() {
        searchTrackState.value = when {
            searchHistoryTrackList.isNotEmpty() -> SearchState.HISTORY_LIST
            _searchRequestMutableData.value.isNotEmpty() -> SearchState.EMPTY_DATA
            else -> SearchState.EMPTY_DATA
        }
    }

    fun clear() {
        _searchTrackList.value = emptyList()
        searchTrackState.value = SearchState.HISTORY_LIST
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val HISTORY_TRACK_LIST_SIZE = 10
    }
}
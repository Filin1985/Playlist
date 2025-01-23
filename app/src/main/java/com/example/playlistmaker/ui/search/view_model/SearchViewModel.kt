package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.search.model.TracksConsumer
import java.util.Collections

class SearchViewModel(
    private val getHistoryTrackListToStorageUseCase: GetTracksHistoryListUseCase,
    private val searchTrackListUseCase: TracksInteractor,
    private val addTrackToHistoryUseCase: AddTracksHistoryListUseCase,
    private val clearTrackHistoryUseCase: ClearTracksHistoryListUseCase
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

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

    private var searchRunnable = Runnable { }

    init {
        if (searchHistoryTrackList.isNotEmpty()) searchTrackState.value = SearchState.HISTORY_LIST
    }

    private fun searchTrackList(searchRequest: String) {
        if (searchRequest.isNotEmpty()) {
            searchTrackState.value = SearchState.SEARCH_PROGRESS
            searchTrackListUseCase.execute(
                text = searchRequest,
                consumer = object : Consumer<List<TrackData>> {
                    override fun consume(data: TracksConsumer<List<TrackData>>) {
                        val consumeRunnable = Runnable {
                            when (data) {
                                is TracksConsumer.Data -> {
                                    searchTrackList.clear()
                                    searchTrackList.addAll(data.value)
                                    searchTrackState.value = SearchState.SUCCESS
                                }

                                is TracksConsumer.EmptyData -> {
                                    searchTrackList.clear()
                                    searchTrackState.value = SearchState.NOT_FOUND
                                }

                                is TracksConsumer.Error -> {
                                    searchTrackList.clear()
                                    searchTrackState.value = SearchState.CONNECTION_ERROR
                                }
                            }
                        }
                        handler.post(consumeRunnable)
                    }
                }
            )
        }
    }

    fun startImmediateSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            handler.removeCallbacks(searchRunnable)
            searchTrackList(searchRequest)
        }
        searchRequestMutableData.value = searchRequest
    }

    fun startDebounceSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.EMPTY_DATA
        } else {
            handler.removeCallbacks(searchRunnable)
            searchRunnable = Runnable { searchTrackList(searchRequest) }
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            searchTrackState.value = SearchState.SEARCH_PROGRESS
        }
        searchRequestMutableData.value = searchRequest
    }

    fun clickOnTrackDebounce(track: TrackData) {
        if (isClickAllowedLiveData.value!!) {
            isClickAllowedMutableLiveData.value = false
            saveHistoryToStorage(track)
            handler.postDelayed(
                { isClickAllowedMutableLiveData.value = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
    }

    fun saveHistoryToStorage(track: TrackData) {
        addTrackToHistoryUseCase.execute(track)
    }

    fun writeTrackToList(track: TrackData) {
        val trackIndex = searchHistoryTrackList.indexOfFirst { it.trackId == track.trackId }
        if (trackIndex != -1) {
            Collections.swap(searchHistoryTrackList, trackIndex, searchHistoryTrackList.size-1)
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

    fun cancelSearch() {
        handler.removeCallbacks(searchRunnable)
        searchRequestMutableData.value = EMPTY_SEARCH
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val EMPTY_SEARCH = ""
        private const val HISTORY_TRACK_LIST_SIZE = 10
    }
}
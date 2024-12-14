package com.example.playlistmaker.ui.search.view_model

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.search.model.TracksConsumer

class SearchViewModel : ViewModel() {
    private val searchCreator by lazy { Creator.getSearchCreator() }

    private val getHistoryTrackListToStorageUseCase by lazy {
        searchCreator.getSearchHistoryStorage()
    }

    private val searchTrackListUseCase by lazy {
        Creator.provideSearchTrackInteractor()
    }

    private val addTrackToHistoryUseCase by lazy {
        searchCreator.addSearchHistoryStorage()
    }

    private val clearTrackHistoryUseCase by lazy {
        searchCreator.clearSearchHistoryStorage()
    }

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
    val searchHistoryTrackListLiveData: LiveData<MutableList<TrackData>> = historyTrackListMutableData

    private val searchRequestMutableData = MutableLiveData<String>()
    val searchRequestLiveData: LiveData<String> = searchRequestMutableData

    private val isClickAllowedMutableLiveData = MutableLiveData(true)
    val isClickAllowedLiveData: LiveData<Boolean> = isClickAllowedMutableLiveData

    private var searchRunnable = Runnable { }

    init {
        if(searchHistoryTrackList.isNotEmpty()) searchTrackState.value = SearchState.HISTORY_LIST
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
        if(searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.NOT_FOUND
        } else {
            handler.removeCallbacks(searchRunnable)
            searchTrackList(searchRequest)
        }
        searchRequestMutableData.value = searchRequest
    }

    fun startDebounceSearch(searchRequest: String) {
        if (searchRequest.isBlank()) {
            searchTrackList.clear()
            searchTrackState.value = SearchState.NOT_FOUND
        } else {
            handler.removeCallbacks(searchRunnable)
            searchRunnable = Runnable { searchTrackList(searchRequest) }
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            searchTrackState.value = SearchState.SEARCH_PROGRESS
        }
        searchRequestMutableData.value = searchRequest
    }

    fun clickOnTrackDebounce(track: TrackData) {
        if(isClickAllowedLiveData.value!!) {
            isClickAllowedMutableLiveData.value = false
            saveHistoryToStorage(track)
            handler.postDelayed({isClickAllowedMutableLiveData.value = true}, CLICK_DEBOUNCE_DELAY)
        }
    }

    fun saveHistoryToStorage(track: TrackData) {
        addTrackToHistoryUseCase.execute(track)
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

    fun setState(currentState: SearchState) {
        searchTrackState.value = currentState
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel()
                }
            }
        }
    }
}
package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.SearchState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onTrackClick: (TrackData) -> Unit
) {
    val searchState by viewModel.liveDataState.collectAsState(SearchState.EMPTY_DATA)
    val searchHistory by viewModel.searchHistoryTrackListLiveData.collectAsState()
    val searchResults by viewModel.searchTrackListLiveData.collectAsState(emptyList())
    val searchQuery by viewModel.searchRequestLiveData.collectAsState()
    val isClickAllowed by viewModel.isClickAllowedLiveData.collectAsState(true)

    var hasFocus by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(
                color = colorResource(R.color.white_day_black_night),
            )
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextFieldWithViewModel(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        when (searchState) {
            SearchState.SUCCESS -> {
                TrackList(
                    tracks = searchResults,
                    onTrackClick = { track ->
                        if (isClickAllowed) {
                            viewModel.clickOnTrackDebounce(track)
                            onTrackClick(track)
                            viewModel.writeTrackToList(track)
                        }
                    }
                )
            }

            SearchState.NOT_FOUND -> {
                NotFoundMessage()
            }

            SearchState.SEARCH_PROGRESS -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            SearchState.CONNECTION_ERROR -> {
                ConnectionError(
                    onRetry = { viewModel.startImmediateSearch(viewModel.searchRequestLiveData.value) }
                )
            }

            SearchState.HISTORY_LIST -> {
                if (searchHistory.isNotEmpty() && (hasFocus || searchQuery.isEmpty())) {
                    HistoryTrackList(
                        tracks = searchHistory,
                        onTrackClick = { track ->
                            if (isClickAllowed) {
                                viewModel.clickOnTrackDebounce(track)
                                onTrackClick(track)
                                viewModel.writeTrackToList(track)
                            }
                        },
                        onClearHistory = { viewModel.clearHistory() }
                    )
                }
            }

            SearchState.EMPTY_DATA -> {
            }
        }
    }
}

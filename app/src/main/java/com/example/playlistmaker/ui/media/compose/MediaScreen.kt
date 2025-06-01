package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.playlistmaker.R
import kotlinx.coroutines.launch
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModelStateFlow
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModelStateFlow

@Composable
fun MediaScreen(
    favouriteViewModel: MediaFavouriteViewModelStateFlow,
    onTrackClick: (TrackData) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    onAddNewPlaylistClick: () -> Unit,
    playlistViewModel: MediaPlaylistViewModelStateFlow
) {
    val tabs = listOf(stringResource(R.string.favourite_tracks), stringResource(R.string.playlist))
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                favouriteViewModel.loadFavoriteTracks()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(R.color.white_day_black_night),
            )
            .padding(16.dp)
    ) {

        TabLayoutComposable(
            tabs = tabs,
            selectedTabIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) { page ->
            when (page) {
                0 -> FavoriteScreen(favouriteViewModel, onTrackClick)

                1 -> PlaylistScreen(playlistViewModel, onPlaylistClick, onAddNewPlaylistClick)
            }
        }
    }
}

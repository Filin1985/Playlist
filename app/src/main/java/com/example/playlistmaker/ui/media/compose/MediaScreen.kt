package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModel
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModel
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModelStateFlow
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModelStateFlow
import com.example.playlistmaker.ui.search.compose.SongItem

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
        // Your mediaText equivalent would go here

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

@Composable
fun FavoriteScreen(viewModel: MediaFavouriteViewModelStateFlow, onTrackClick: (TrackData) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val isClickAllowed by viewModel.isClickAllowed.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is MediaFavouriteViewModelStateFlow.UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is MediaFavouriteViewModelStateFlow.UiState.Empty -> {
                EmptyFavoritesContent()
            }

            is MediaFavouriteViewModelStateFlow.UiState.Success -> {
                val successState = uiState as MediaFavouriteViewModelStateFlow.UiState.Success
                TrackListContent(
                    tracks = successState.tracks,
                    isClickAllowed = isClickAllowed,
                    onTrackClick = onTrackClick,
                    onDebounceClick = { viewModel.handleTrackClick() }
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_not_found_dark),
            contentDescription = "Empty favorites",
            modifier = Modifier.size(164.dp)
        )
        Text(
            stringResource(R.string.empty_media), style = TextStyle(
                color = colorResource(R.color.black_day_white_night),
                fontSize = 19.sp,
                fontFamily = FontFamily(
                    Font(R.font.ys_display_medium, weight = FontWeight.W400)
                )
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun TrackListContent(
    tracks: List<TrackData>,
    isClickAllowed: Boolean,
    onTrackClick: (TrackData) -> Unit,
    onDebounceClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true,
        verticalArrangement = Arrangement.Top
    ) {
        itemsIndexed(tracks) { _, track ->
            SongItem(track = track, onClick = {
                if (isClickAllowed) {
                    onTrackClick(track)
                    onDebounceClick()
                }
            })
        }
    }
}


@Composable
fun PlaylistScreen(
    viewModel: MediaPlaylistViewModelStateFlow,
    onPlaylistClick: (Playlist) -> Unit,
    onAddNewPlaylistClick: () -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()
    val playlistStatus by viewModel.playlistStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.showPlaylists()
    }

    MediaPlaylistContent(playlistStatus, playlists, onPlaylistClick, onAddNewPlaylistClick)
}

@Composable
private fun MediaPlaylistContent(
    playlistStatus: PlaylistStatus<List<Playlist>>,
    playlists: List<Playlist>,
    onPlaylistClick: (Playlist) -> Unit,
    onAddNewPlaylistClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                modifier = Modifier
                    .background(
                        colorResource(R.color.black_day_white_night),
                        shape = RoundedCornerShape(54.dp)
                    ),
                onClick = onAddNewPlaylistClick,
                contentPadding = PaddingValues(horizontal = 14.dp),
            ) {
                Text(
                    text = stringResource(R.string.new_playlist),
                    style = TextStyle(
                        color = colorResource(R.color.white_day_black_night),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(R.font.ys_display_medium, weight = FontWeight.W500)
                        )
                    ),
                )
            }
        }

        Box() {
            when (playlistStatus) {
                is PlaylistStatus.Content -> {
                    PlaylistGrid(
                        playlists = playlists,
                        onPlaylistClick = onPlaylistClick,
                    )
                }

                is PlaylistStatus.Empty -> {
                    EmptyPlaylistState()
                }
            }
        }
    }
}

@Composable
private fun PlaylistGrid(
    playlists: List<Playlist>,
    onPlaylistClick: (Playlist) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(playlists) { _, playlist ->
            PlaylistItem(
                playlist = playlist,
                onClick = { onPlaylistClick(playlist) },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp),
        onClick = onClick
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.coverUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Album artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(160.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                placeholder = painterResource(R.drawable.default_art_work),
                error = painterResource(R.drawable.default_art_work)
            )

            Text(
                text = playlist.title,
                style = TextStyle(
                    color = colorResource(R.color.black_day_white_night),
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Start
            )

            Text(
                text = getTrackText(playlist.size),
                style = TextStyle(
                    color = colorResource(R.color.black_day_white_night),
                    fontSize = 16.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

        }

    }
}

@Composable
private fun EmptyPlaylistState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_not_found_dark),
            contentDescription = "Empty favorites",
            modifier = Modifier.size(164.dp)
        )
        Text(
            stringResource(R.string.empty_playlist), style = TextStyle(
                color = colorResource(R.color.black_day_white_night),
                fontSize = 19.sp,
                fontFamily = FontFamily(
                    Font(R.font.ys_display_medium, weight = FontWeight.W400)
                )
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

fun getTrackText(count: Int): String {
    return when {
        count % 100 in 11..14 -> "$count треков"
        count % 10 == 1 -> "$count трек"
        count % 10 in 2..4 -> "$count трека"
        else -> "$count треков"
    }
}
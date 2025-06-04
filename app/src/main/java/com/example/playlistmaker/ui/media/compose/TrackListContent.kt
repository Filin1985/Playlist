package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.search.compose.SongItem

@Composable
fun TrackListContent(
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
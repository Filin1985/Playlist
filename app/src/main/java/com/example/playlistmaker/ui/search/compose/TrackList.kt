package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.playlistmaker.domain.search.model.TrackData

@Composable
fun TrackList(
    tracks: List<TrackData>,
    onTrackClick: (TrackData) -> Unit
) {
    LazyColumn {
        itemsIndexed(tracks) { _, track ->
            SongItem(track = track, onClick = { onTrackClick(track) })
        }
    }
}
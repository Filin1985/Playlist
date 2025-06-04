package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.TrackData

@Composable
fun HistoryTrackList(
    tracks: List<TrackData>,
    onTrackClick: (TrackData) -> Unit,
    onClearHistory: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.history_search),
                style = TextStyle(
                    color = colorResource(R.color.black_day_white_night),
                    fontSize = 19.sp,
                    fontFamily = FontFamily(
                        Font(R.font.ys_display_medium, weight = FontWeight.W500)
                    )
                ),
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyColumn {
            itemsIndexed(tracks) { _, track ->
                SongItem(track = track, onClick = { onTrackClick(track) })
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                modifier = Modifier
                    .background(
                        colorResource(R.color.black_day_white_night),
                        shape = RoundedCornerShape(54.dp)
                    ),
                onClick = onClearHistory,
                contentPadding = PaddingValues(horizontal = 14.dp),
            ) {
                Text(
                    text = stringResource(R.string.history_clear),
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
    }
}
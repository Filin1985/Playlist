package com.example.playlistmaker.ui.search.compose

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import com.example.playlistmaker.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.domain.search.model.TrackData
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.utils.DateUtils.msToMMSSFormat

@Composable
fun SongItem(
    track: TrackData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .build(),
            contentDescription = "Album artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(45.dp),
            placeholder = painterResource(R.drawable.default_art_work),
            error = painterResource(R.drawable.default_art_work)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = track.trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = colorResource(R.color.black_day_white_night),
                    fontSize = 16.sp,
                )
            )

            // Artist name and duration row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Artist name
                Text(
                    text = track.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = colorResource(R.color.grey2_day_true_white_night),
                        fontSize = 11.sp,
                        fontFamily = FontFamily(
                            Font(R.font.ys_display_regular, weight = FontWeight.W400)
                        )
                    )
                )

                // Dot separator
                Image(
                    painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = "Separator",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(4.dp) // Adjust size as needed
                )

                // Track duration
                Text(
                    text = msToMMSSFormat(track.trackTimeMillis),
                    style = TextStyle(
                        color = colorResource(R.color.grey2_day_true_white_night),
                        fontSize = 11.sp,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Arrow icon
        Image(
            painter = painterResource(id = R.drawable.ic_song_arrow),
            contentDescription = "More options",
        )
    }
}

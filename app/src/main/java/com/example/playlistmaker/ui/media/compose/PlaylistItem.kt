package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.background(colorResource(R.color.white_day_black_night))
        ) {
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
                text = pluralStringResource(R.plurals.playlist_plurals, playlist.size, playlist.size),
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

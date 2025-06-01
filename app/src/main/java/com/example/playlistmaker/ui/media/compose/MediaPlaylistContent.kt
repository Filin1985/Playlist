package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
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
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus

@Composable
fun MediaPlaylistContent(
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
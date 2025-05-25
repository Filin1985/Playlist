package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun NotFoundMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_not_found_dark),
            contentDescription = "Not found",
            modifier = Modifier.size(164.dp)
        )
        Text(
            text = stringResource(R.string.not_found_error),
            style = TextStyle(
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
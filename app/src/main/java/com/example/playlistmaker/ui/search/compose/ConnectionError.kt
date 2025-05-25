package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun ConnectionError(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_connection_err_dark),
            contentDescription = "Connection error",
            modifier = Modifier.size(164.dp)
        )
        Text(
            text = stringResource(R.string.error_connection_subtitle),
            style = TextStyle(
                color = colorResource(R.color.black_day_white_night),
                fontSize = 19.sp,
                fontFamily = FontFamily(
                    Font(R.font.ys_display_medium, weight = FontWeight.W500)
                )
            ),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.error_connection_title),
            style = TextStyle(
                color = colorResource(R.color.black_day_white_night),
                fontSize = 19.sp,
                fontFamily = FontFamily(
                    Font(R.font.ys_display_medium, weight = FontWeight.W500)
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
        TextButton(
            modifier = Modifier
                .background(
                    colorResource(R.color.black_day_white_night),
                    shape = RoundedCornerShape(54.dp)
                ),
            onClick = onRetry,
            contentPadding = PaddingValues(horizontal = 14.dp),
        ) {
            Text(
                text = stringResource(R.string.refresh),
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
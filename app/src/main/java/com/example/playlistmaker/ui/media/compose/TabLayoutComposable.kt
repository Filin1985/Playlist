package com.example.playlistmaker.ui.media.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

@Composable
fun TabLayoutComposable(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow (
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        backgroundColor = colorResource(R.color.white_day_black_night),
        contentColor = colorResource(R.color.black_day_white_night),

    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                        textAlign = TextAlign.Center
                    )
                },
                selectedContentColor = colorResource(R.color.black_day_white_night),
                unselectedContentColor = colorResource(R.color.black_day_white_night),
            )
        }
    }
}
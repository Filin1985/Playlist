package com.example.playlistmaker.ui.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

@Composable
fun TextFieldWithViewModel(viewModel: SearchViewModel) {
    val searchText = viewModel.searchRequestLiveData.collectAsState().value
    Box(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 16.dp)
            .height(36.dp)  // Fixed height
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(R.color.grey1_day_white_night),  // Replace with your search_shape background
                    shape = RoundedCornerShape(8.dp)  // Half of height for pill shape
                )
        )

        BasicTextField(
            value = searchText,
            onValueChange = { newText ->
                viewModel.onSearchRequestChange(newText)
                viewModel.startDebounceSearch(newText)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 36.dp, end = 36.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchText.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            color = colorResource(R.color.grey2_day_black_night),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            },
        )

        // Search icon
        Icon(
            painter = painterResource(R.drawable.ic_search_grey),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
                .size(18.dp),
            tint = colorResource(R.color.grey2_day_black_night)
        )

        // Clear icon
        if (searchText.isNotEmpty()) {
            IconButton(
                onClick = {
                    viewModel.onSearchRequestChange("")
                    viewModel.clear()
                },
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.search),
                    tint = colorResource(R.color.grey2_day_black_night)
                )
            }
        }
    }
}
package com.example.playlistmaker.ui.settings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModelWithStateFLow
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.sharing.model.EmailData
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModelWithStateFLow,
    settingsViewModel: SettingsViewModel,
) {
    val themeState by themeViewModel.themeState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white_day_black_night)).padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = TextStyle(
                color = colorResource(R.color.black_day_white_night),
                fontSize = 22.sp,
                fontFamily = FontFamily(
                    Font(R.font.ys_display_medium, weight = FontWeight.W500)
                )
            ),
            modifier = Modifier.padding(16.dp)
        )

        ThemeSwitcher(
            isDarkTheme = themeState is Theme.DarkTheme,
            onThemeChanged = { isDark ->
                val theme = if (isDark) Theme.DarkTheme() else Theme.LightTheme()
                themeViewModel.switchAndSaveTheme(theme)
            }
        )

        val shareLink = stringResource(R.string.share_link)
        SettingItem(
            title = stringResource(R.string.share),
            icon = ImageVector.vectorResource(id = R.drawable.ic_share),
            onClick = { settingsViewModel.shareApp(shareLink) }
        )

        val supportEmail = stringResource(R.string.support_email)
        val supportSubject = stringResource(R.string.support_subject)
        val supportText = stringResource(R.string.support_text)
        SettingItem(
            title = stringResource(R.string.support),
            icon = ImageVector.vectorResource(id = R.drawable.ic_support),
            onClick = {
                settingsViewModel.mailToSupport(
                    EmailData(
                        email = supportEmail,
                        subject = supportSubject,
                        link = supportText
                    )
                )
            }
        )

        val agreementLink = stringResource(R.string.agreement_link)
        SettingItem(
            title = stringResource(R.string.agreement),
            icon = ImageVector.vectorResource(id = R.drawable.ic_agreement),
            onClick = { settingsViewModel.openTerms(agreementLink) }
        )
    }

    LaunchedEffect(Unit) {
        themeViewModel.getPrevTheme()
    }
}

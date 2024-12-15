package com.example.playlistmaker.data.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.sharing.SharedPrefRepository
import com.example.playlistmaker.domain.sharing.ThemeRepository
import com.example.playlistmaker.domain.settings.model.Theme

class ThemeRepositoryImpl(private val themeStorage: SharedPrefRepository): ThemeRepository {
    override fun getPrevTheme(): Theme {
        return themeStorage.getPrevTheme()
    }

    override fun saveTheme(theme: Theme) {
        themeStorage.saveTheme(theme)
    }

    override fun switchTheme(theme: Theme) {
        val themValue = when(theme) {
            is Theme.LightTheme -> AppCompatDelegate.MODE_NIGHT_NO
            is Theme.DarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(themValue)
    }
}
package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.settings.model.Theme

interface ThemeRepository {
    fun getPrevTheme(): Theme
    fun switchTheme(theme: Theme)
    fun saveTheme(theme: Theme)
}
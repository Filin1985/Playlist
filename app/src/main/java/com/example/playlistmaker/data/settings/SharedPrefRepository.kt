package com.example.playlistmaker.data.settings

import com.example.playlistmaker.domain.settings.model.Theme

interface SharedPrefRepository {
    fun saveTheme(theme: Theme)
    fun getPrevTheme(): Theme
}
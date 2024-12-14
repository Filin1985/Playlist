package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.settings.model.Theme

interface SharedPrefRepository {
    fun saveTheme(theme: Theme)
    fun getPrevTheme(): Theme
}
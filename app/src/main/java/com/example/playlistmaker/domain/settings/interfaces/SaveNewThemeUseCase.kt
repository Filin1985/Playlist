package com.example.playlistmaker.domain.settings.interfaces

import com.example.playlistmaker.domain.settings.model.Theme

interface SaveNewThemeUseCase {
    fun execute(theme: Theme)
}
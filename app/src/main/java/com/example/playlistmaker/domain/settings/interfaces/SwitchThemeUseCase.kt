package com.example.playlistmaker.domain.settings.interfaces

import com.example.playlistmaker.domain.settings.model.Theme

interface SwitchThemeUseCase {
    fun execute(theme: Theme)
}
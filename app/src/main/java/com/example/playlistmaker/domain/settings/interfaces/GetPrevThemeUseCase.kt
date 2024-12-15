package com.example.playlistmaker.domain.settings.interfaces

import com.example.playlistmaker.domain.settings.model.Theme

interface GetPrevThemeUseCase {
    fun execute(): Theme
}
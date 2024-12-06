package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.ThemeRepository
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.model.Theme

class SaveNewThemeUseCaseImpl(private val themeRepository: ThemeRepository): SaveNewThemeUseCase {
    override fun execute(theme: Theme) {
        themeRepository.saveTheme(theme)
    }
}
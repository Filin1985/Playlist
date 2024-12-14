package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.sharing.ThemeRepository
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase
import com.example.playlistmaker.domain.settings.model.Theme

class SwitchThemeUseCaseImpl(private val themeRepository: ThemeRepository): SwitchThemeUseCase {
    override fun execute(theme: Theme) {
        themeRepository.switchTheme(theme)
    }
}
package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.ThemeRepository
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.model.Theme

class GetPrevThemeUseCaseImpl(private val themeRepository: ThemeRepository): GetPrevThemeUseCase {
    override fun execute(): Theme {
        return themeRepository.getPrevTheme()
    }
}
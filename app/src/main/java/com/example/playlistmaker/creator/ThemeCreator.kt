package com.example.playlistmaker.creator

import com.example.playlistmaker.data.settings.ThemeRepository
import com.example.playlistmaker.domain.settings.impl.GetPrevThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SaveNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase

class ThemeCreator(private val themeRepository: ThemeRepository) {
    fun saveNewTheme(): SaveNewThemeUseCase {
        return SaveNewThemeUseCaseImpl(themeRepository)
    }

    fun switchNewTheme(): SwitchThemeUseCase {
        return SwitchThemeUseCaseImpl(themeRepository)
    }

    fun getPrevTheme(): GetPrevThemeUseCase {
        return GetPrevThemeUseCaseImpl(themeRepository)
    }
}
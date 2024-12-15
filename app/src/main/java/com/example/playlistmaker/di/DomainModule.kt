package com.example.playlistmaker.di

import com.example.playlistmaker.domain.settings.impl.GetPrevThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SaveNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase
import org.koin.dsl.module

val domainModule = module {
    single<SaveNewThemeUseCase> {
        SaveNewThemeUseCaseImpl(get())
    }

    single<GetPrevThemeUseCase> {
        GetPrevThemeUseCaseImpl(get())
    }

    single<SwitchThemeUseCase> {
        SwitchThemeUseCaseImpl(get())
    }
}
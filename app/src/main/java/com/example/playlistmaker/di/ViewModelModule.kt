package com.example.playlistmaker.di

import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import org.koin.dsl.module

val viewModuleModule = module {
    single<ThemeViewModel> {
        ThemeViewModel(get(), get(), get())
    }
}
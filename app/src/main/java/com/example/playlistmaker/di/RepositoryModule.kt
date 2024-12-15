package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.impl.SharedPrefThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.example.playlistmaker.domain.sharing.SharedPrefRepository
import com.example.playlistmaker.domain.sharing.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SharedPrefThemeRepositoryImpl.DARK_THEME,
            Context.MODE_PRIVATE
        )
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<SharedPrefRepository> {
        SharedPrefThemeRepositoryImpl(get(), get())
    }
}
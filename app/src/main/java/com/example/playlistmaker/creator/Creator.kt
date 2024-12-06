package com.example.playlistmaker.creator

import android.app.Application
import android.util.Log
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.ThemeRepository
import com.example.playlistmaker.data.settings.impl.SharedPrefThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.settings.impl.GetPrevThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SaveNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase

object Creator {
    lateinit var application: Application
    fun registryApplication(application: Application) {
        this.application = application
    }

    fun provideSearchTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(provideSearchRepository())
    }

    fun getPlayerCreator(track: TrackData): PlayerCreator {
        return PlayerCreator(track)
    }

    fun getSearchCreator(): SearchCreator {
        return SearchCreator()
    }

    fun getSettingsCreator(): SettingsCreator {
        return SettingsCreator(provideThemeRepository())
    }

    private fun getRetrofitNetworkClient() = RetrofitNetworkClient()

    private fun provideSearchRepository() =
        TracksRepositoryImpl(getRetrofitNetworkClient())

//    fun saveNewTheme(): SaveNewThemeUseCase {
//        return SaveNewThemeUseCaseImpl(provideThemeRepository())
//    }
//
//    fun switchNewTheme(): SwitchThemeUseCase {
//        return SwitchThemeUseCaseImpl(provideThemeRepository())
//    }
//
//    fun getPrevTheme(): GetPrevThemeUseCase {
//        return GetPrevThemeUseCaseImpl(provideThemeRepository())
//    }

    private fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedPrefThemeRepositoryImpl(application))
    }
}
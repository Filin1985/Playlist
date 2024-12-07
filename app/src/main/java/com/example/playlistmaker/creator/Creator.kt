package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.ThemeRepository
import com.example.playlistmaker.data.settings.impl.SharedPrefThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.model.TrackData

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

    fun getThemeCreator(): ThemeCreator {
        return ThemeCreator(provideThemeRepository())
    }

    fun getSettingsCreator(): SettingsCreator {
        return SettingsCreator(provideExternalNavigator())
    }

    private fun getRetrofitNetworkClient() = RetrofitNetworkClient()

    private fun provideSearchRepository() =
        TracksRepositoryImpl(getRetrofitNetworkClient())

    private fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedPrefThemeRepositoryImpl(application))
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }
}
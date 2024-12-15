package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModuleModule
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val saveNewTheme: SaveNewThemeUseCase by inject()
    private val getPrevTheme: GetPrevThemeUseCase by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(repositoryModule, domainModule, viewModuleModule)
        }
        saveNewTheme.execute(getPrevTheme.execute())
    }

    companion object {
        const val SETTINGS = "SETTINGS"
    }
}
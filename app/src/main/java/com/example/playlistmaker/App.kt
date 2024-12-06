package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App : Application() {

    private val saveNewTheme by lazy { Creator.getSettingsCreator().saveNewTheme() }
    private val getPrevTheme by lazy { Creator.getSettingsCreator().getPrevTheme() }

    override fun onCreate() {
        super.onCreate()
        Creator.registryApplication(this)
        saveNewTheme.execute(getPrevTheme.execute())
    }

    companion object {
        const val SETTINGS = "SETTINGS"
    }
}
package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

val DARK_THEME = "DARK_THEME"

class App : Application() {

    companion object {
        const val SETTINGS = "SETTINGS"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(SETTINGS, MODE_PRIVATE)
        Log.d("MY_TAG", "onCreate isDarkTheme(this) ${isDarkTheme(this)}")

        darkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        Log.d("MY_TAG", "onCreate darkTheme ${darkTheme}")
        switchTheme(darkTheme)
    }

    private fun isDarkTheme(context: Context): Boolean {
        val isDarkMode: Boolean
        val currentNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDarkMode = when ((currentNightMode)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        return isDarkMode
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkThemeEnabled)
    }

    fun saveTheme(darkThemeEnabled: Boolean){
        getSharedPreferences(SETTINGS, MODE_PRIVATE).edit()
            .putBoolean(DARK_THEME, darkThemeEnabled)
            .apply()
    }
}
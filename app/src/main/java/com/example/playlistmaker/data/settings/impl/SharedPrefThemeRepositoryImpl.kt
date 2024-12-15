package com.example.playlistmaker.data.settings.impl

import android.content.Context
import android.content.res.Configuration
import com.example.playlistmaker.domain.sharing.SharedPrefRepository
import com.example.playlistmaker.domain.settings.model.Theme

class SharedPrefThemeRepositoryImpl(private val context: Context): SharedPrefRepository {
    private val sharedPreferences = context.getSharedPreferences(THEME_SETTINGS, Context.MODE_PRIVATE)

    override fun getPrevTheme(): Theme {
        val isDarkTheme = sharedPreferences.getBoolean(
            DARK_THEME,
            getSystemNightMode(context)
        )
        return if (isDarkTheme) Theme.DarkTheme() else Theme.LightTheme()
    }

    override fun saveTheme(theme: Theme){
        val isDarkTheme = when (theme) {
            is Theme.LightTheme -> false
            is Theme.DarkTheme -> true
        }

        sharedPreferences.edit()
            .putBoolean(DARK_THEME, isDarkTheme)
            .apply()
    }

    private fun getSystemNightMode(context: Context) : Boolean {
        return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }
            else -> {
                false
            }
        }
    }

    companion object {
        const val THEME_SETTINGS = "THEME_SETTINGS"
        const val DARK_THEME = "DARK_THEME"
    }
}
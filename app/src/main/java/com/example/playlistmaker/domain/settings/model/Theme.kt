package com.example.playlistmaker.domain.settings.model

sealed interface Theme {
    class DarkTheme() : Theme
    class LightTheme() : Theme
}
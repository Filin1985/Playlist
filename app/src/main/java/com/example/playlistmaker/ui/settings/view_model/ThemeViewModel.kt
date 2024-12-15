package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase
import com.example.playlistmaker.domain.settings.model.Theme

class ThemeViewModel(
    private val saveThemeUseCase: SaveNewThemeUseCase,
    private val getPrevThemeUseCase: GetPrevThemeUseCase,
    private val switchThemeUseCase: SwitchThemeUseCase
) : ViewModel() {


    private val mutableLiveData = MutableLiveData<Theme>()
    val liveData: LiveData<Theme> = mutableLiveData

    fun switchAndSaveTheme(theme: Theme) {
        mutableLiveData.value = theme
        switchThemeUseCase.execute(theme)
        saveThemeUseCase.execute(theme)
    }

    fun getPrevTheme() {
        mutableLiveData.value = getPrevThemeUseCase.execute()
    }
}
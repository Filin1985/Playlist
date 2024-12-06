package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.model.Theme

class SettingsViewModel: ViewModel() {
    private val saveThemeUseCase by lazy { Creator.getSettingsCreator().saveNewTheme() }
    private val getPrevThemeUseCase by lazy { Creator.getSettingsCreator().getPrevTheme() }
    private val switchThemeUseCase by lazy { Creator.getSettingsCreator().switchNewTheme() }

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

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel()
                }
            }
        }
    }
}
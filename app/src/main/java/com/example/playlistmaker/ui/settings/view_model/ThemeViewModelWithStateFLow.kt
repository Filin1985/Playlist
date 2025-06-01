package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase
import com.example.playlistmaker.domain.settings.model.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModelWithStateFLow(
    private val saveThemeUseCase: SaveNewThemeUseCase,
    private val getPrevThemeUseCase: GetPrevThemeUseCase,
    private val switchThemeUseCase: SwitchThemeUseCase
) : ViewModel() {


    private val _themeState = MutableStateFlow<Theme?>(null)
    val themeState: StateFlow<Theme?> = _themeState.asStateFlow()

    fun switchAndSaveTheme(theme: Theme) {
        viewModelScope.launch {
            _themeState.value = theme
            switchThemeUseCase.execute(theme)
            saveThemeUseCase.execute(theme)
        }
    }

    fun getPrevTheme() {
        viewModelScope.launch {
            _themeState.value = getPrevThemeUseCase.execute()
        }
    }
}
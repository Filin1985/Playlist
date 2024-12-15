package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.sharing.model.EmailData

class SettingsViewModel: ViewModel() {
    private val shareAppUseCase by lazy { Creator.getSettingsCreator().shareApp() }
    private val openTermsUseCase by lazy { Creator.getSettingsCreator().openTerms() }
    private val mailToSupportUseCase by lazy { Creator.getSettingsCreator().mailToSupport() }

    fun shareApp(link: String) {
        shareAppUseCase.execute(link)
    }

    fun openTerms(link: String) {
        openTermsUseCase.execute(link)
    }

    fun mailToSupport(emailData: EmailData) {
        mailToSupportUseCase.execute(emailData)
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
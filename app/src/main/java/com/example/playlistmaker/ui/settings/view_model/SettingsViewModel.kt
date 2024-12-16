package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.sharing.interfaces.MailToSupportUseCase
import com.example.playlistmaker.domain.sharing.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.interfaces.SharingAppUseCase
import com.example.playlistmaker.domain.sharing.model.EmailData

class SettingsViewModel(
    private val shareAppUseCase: SharingAppUseCase,
    private val openTermsUseCase: OpenTermsUseCase,
    private val mailToSupportUseCase: MailToSupportUseCase
) : ViewModel() {
    fun shareApp(link: String) {
        shareAppUseCase.execute(link)
    }

    fun openTerms(link: String) {
        openTermsUseCase.execute(link)
    }

    fun mailToSupport(emailData: EmailData) {
        mailToSupportUseCase.execute(emailData)
    }
}
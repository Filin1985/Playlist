package com.example.playlistmaker.creator

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.impl.MailToSupportUseCaseImpl
import com.example.playlistmaker.domain.sharing.impl.OpenTermsUseCaseImpl
import com.example.playlistmaker.domain.sharing.impl.SharingAppUseCaseImpl
import com.example.playlistmaker.domain.sharing.interfaces.MailToSupportUseCase
import com.example.playlistmaker.domain.sharing.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.interfaces.SharingAppUseCase

class SettingsCreator(private val externalNavigator: ExternalNavigator) {
    fun shareApp(): SharingAppUseCase {
        return SharingAppUseCaseImpl(externalNavigator)
    }

    fun mailToSupport(): MailToSupportUseCase {
        return MailToSupportUseCaseImpl(externalNavigator)
    }

    fun openTerms(): OpenTermsUseCase {
        return OpenTermsUseCaseImpl(externalNavigator)
    }
}
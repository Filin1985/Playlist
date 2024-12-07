package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.dto.ExternalNavigationData
import com.example.playlistmaker.domain.sharing.interfaces.MailToSupportUseCase
import com.example.playlistmaker.domain.sharing.model.EmailData

class MailToSupportUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    MailToSupportUseCase {
    override fun execute() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = ExternalNavigationData.supportEmail,
            subject = ExternalNavigationData.supportEmailDefaultSubject,
            text = ExternalNavigationData.supportEmailDefaultText
        )
    }
}
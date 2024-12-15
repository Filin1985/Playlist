package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.interfaces.MailToSupportUseCase
import com.example.playlistmaker.domain.sharing.model.EmailData

class MailToSupportUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    MailToSupportUseCase {
    override fun execute(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}
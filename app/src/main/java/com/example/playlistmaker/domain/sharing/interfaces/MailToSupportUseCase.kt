package com.example.playlistmaker.domain.sharing.interfaces

import com.example.playlistmaker.domain.sharing.model.EmailData

interface MailToSupportUseCase {
    fun execute(emailData: EmailData)
}
package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.interfaces.OpenTermsUseCase

class OpenTermsUseCaseImpl(private val externalNavigator: ExternalNavigator) : OpenTermsUseCase {
    override fun execute(link: String) {
        externalNavigator.openLink(link)
    }
}
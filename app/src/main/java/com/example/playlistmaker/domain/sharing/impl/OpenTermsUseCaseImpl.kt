package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.dto.ExternalNavigationData
import com.example.playlistmaker.domain.sharing.interfaces.OpenTermsUseCase

class OpenTermsUseCaseImpl(private val externalNavigator: ExternalNavigator) : OpenTermsUseCase {
    override fun execute() {
        externalNavigator.openLink(getTermsLink())
    }

    private fun getTermsLink(): String {
        return ExternalNavigationData.termsLink
    }
}
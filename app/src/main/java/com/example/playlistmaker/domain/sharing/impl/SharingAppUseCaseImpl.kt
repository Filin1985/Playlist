package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.dto.ExternalNavigationData
import com.example.playlistmaker.domain.sharing.interfaces.SharingAppUseCase

class SharingAppUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    SharingAppUseCase {
    override fun execute() {
        externalNavigator.shareLink(getShareAppLink())
    }

    private fun getShareAppLink(): String {
        return ExternalNavigationData.shareAppLink
    }
}
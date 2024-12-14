package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.interfaces.SharingAppUseCase

class SharingAppUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    SharingAppUseCase {
    override fun execute(link: String) {
        externalNavigator.shareLink(link)
    }
}
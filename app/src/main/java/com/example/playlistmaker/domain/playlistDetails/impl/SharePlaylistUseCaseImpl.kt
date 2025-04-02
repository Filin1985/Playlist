package com.example.playlistmaker.domain.playlistDetails.impl

import com.example.playlistmaker.domain.playlistDetails.interfaces.SharePlaylistUseCase
import com.example.playlistmaker.domain.sharing.ExternalNavigator

class SharePlaylistUseCaseImpl(private val externalNavigator: ExternalNavigator): SharePlaylistUseCase {
    override fun execute(playlistMessage: String) {
        externalNavigator.sendMessage(playlistMessage)
    }
}
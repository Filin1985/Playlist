package com.example.playlistmaker.domain.playlistDetails.interfaces

interface SharePlaylistUseCase {
    fun execute(playlistMessage: String)
}
package com.example.playlistmaker.domain.playlistDetails.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlistDetails.interfaces.DeletePlaylistUseCase

class DeletePlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository): DeletePlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }
}
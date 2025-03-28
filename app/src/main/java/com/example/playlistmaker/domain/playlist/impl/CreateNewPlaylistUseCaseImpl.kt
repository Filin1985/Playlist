package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.CreateNewPlaylistUseCase

class CreateNewPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) :
    CreateNewPlaylistUseCase {
    override suspend fun execute(playlist: Playlist) {
        playlistsRepository.createPlaylist(playlist)
    }
}
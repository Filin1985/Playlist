package com.example.playlistmaker.domain.playlistDetails.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlistDetails.interfaces.GetPlaylistByIdUseCase

class GetPlaylistByIdUseCaseImpl(private val playlistRepository: PlaylistsRepository): GetPlaylistByIdUseCase {
    override suspend fun execute(playlistId: Int): Playlist {
        return playlistRepository.getPlaylistById(playlistId)
    }
}
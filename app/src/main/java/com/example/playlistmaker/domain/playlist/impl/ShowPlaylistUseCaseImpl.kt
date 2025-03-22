package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.ShowPlaylistUseCase
import kotlinx.coroutines.flow.Flow

class ShowPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository): ShowPlaylistUseCase {
    override fun execute(): Flow<List<Playlist>> {
        return playlistsRepository.showPlaylists()
    }
}
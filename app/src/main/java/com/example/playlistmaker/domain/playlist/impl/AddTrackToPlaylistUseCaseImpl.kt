package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.playlist.interfaces.AddTrackToPlaylistUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class AddTrackToPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) : AddTrackToPlaylistUseCase {
    override suspend fun execute(track: TrackData) {
        playlistsRepository.addTrackToPlaylistsStorage(track)
    }
}
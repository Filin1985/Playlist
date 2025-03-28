package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.interfaces.UpdatePlaylistUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class UpdatePlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository): UpdatePlaylistUseCase {
    override suspend fun execute(playlist: Playlist, track: TrackData) {
        playlistsRepository.updatePlaylist(playlist, track)
    }
}
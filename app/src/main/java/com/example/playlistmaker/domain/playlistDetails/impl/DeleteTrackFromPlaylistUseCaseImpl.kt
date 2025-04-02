package com.example.playlistmaker.domain.playlistDetails.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlistDetails.interfaces.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class DeleteTrackFromPlaylistUseCaseImpl(private val playlistRepository: PlaylistsRepository): DeleteTrackFromPlaylistUseCase {
    override suspend fun execute(track: TrackData, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(track, playlist)
    }
}
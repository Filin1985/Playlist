package com.example.playlistmaker.domain.playlistDetails.impl

import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.playlistDetails.interfaces.GetTracksFromPlaylistUseCase
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

class GetTracksFromPlaylistUseCaseImpl(private val playlistsRepository: PlaylistsRepository) :
    GetTracksFromPlaylistUseCase {
    override fun execute(tracksId: List<String>): Flow<List<TrackData>> {
        return playlistsRepository.getTracksFromPlaylist(tracksId)
    }
}
package com.example.playlistmaker.domain.playlistDetails.interfaces

import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface GetTracksFromPlaylistUseCase {
    fun execute(tracksId: List<String>): Flow<List<TrackData>>
}
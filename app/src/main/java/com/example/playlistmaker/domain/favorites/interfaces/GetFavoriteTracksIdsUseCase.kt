package com.example.playlistmaker.domain.favorites.interfaces

import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface GetFavoriteTracksIdsUseCase {
    suspend fun execute(): Flow<List<Long>>
}
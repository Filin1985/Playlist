package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    suspend fun insertFavoriteTrack(track: TrackData)

    suspend fun deleteFavoriteTrack(track: TrackData)

    fun getFavoriteTracks() : Flow<List<TrackData>>

    fun getFavoriteTracksIds() : Flow<List<Long>>
}
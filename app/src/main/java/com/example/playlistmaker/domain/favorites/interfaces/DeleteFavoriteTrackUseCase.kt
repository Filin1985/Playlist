package com.example.playlistmaker.domain.favorites.interfaces

import com.example.playlistmaker.domain.search.model.TrackData

interface DeleteFavoriteTrackUseCase {
    suspend fun deleteFavoriteTrack(track: TrackData)
}
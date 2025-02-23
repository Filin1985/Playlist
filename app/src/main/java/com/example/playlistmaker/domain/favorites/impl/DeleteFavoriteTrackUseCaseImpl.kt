package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.interfaces.DeleteFavoriteTrackUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class DeleteFavoriteTrackUseCaseImpl(private val favoriteRepository: FavoriteTracksRepository) :
    DeleteFavoriteTrackUseCase {
    override suspend fun execute(track: TrackData) {
        favoriteRepository.deleteFavoriteTrack(track)
    }
}
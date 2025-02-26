package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.interfaces.InsertFavoriteTrackUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class InsertFavoriteTrackUseCaseImpl(private val favoriteRepository: FavoriteTracksRepository) :
    InsertFavoriteTrackUseCase {
    override suspend fun execute(track: TrackData) {
        favoriteRepository.insertFavoriteTrack(track)
    }
}
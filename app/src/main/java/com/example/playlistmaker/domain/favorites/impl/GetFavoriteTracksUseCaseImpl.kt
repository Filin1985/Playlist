package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.interfaces.GetFavoriteTracksUseCase
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

class GetFavoriteTracksUseCaseImpl(private val favoritesRepository: FavoriteTracksRepository) :
    GetFavoriteTracksUseCase {
    override suspend fun execute(): Flow<List<TrackData>> {
        return favoritesRepository.getFavoriteTracks()
    }
}
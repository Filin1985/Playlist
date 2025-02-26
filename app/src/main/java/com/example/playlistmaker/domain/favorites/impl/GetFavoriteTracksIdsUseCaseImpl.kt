package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.favorites.interfaces.GetFavoriteTracksIdsUseCase
import kotlinx.coroutines.flow.Flow

class GetFavoriteTracksIdsUseCaseImpl(private val favoritesRepository: FavoriteTracksRepository) :
    GetFavoriteTracksIdsUseCase {
    override suspend fun execute(): Flow<List<String>> {
        return favoritesRepository.getFavoriteTracksIds()
    }
}
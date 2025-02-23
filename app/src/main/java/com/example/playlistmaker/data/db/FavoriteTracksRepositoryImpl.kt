package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.convertes.TrackDbConverter
import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val favoriteTracksDB: FavoriteTracksDB) :
    FavoriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<TrackData>> = flow {
        val tracks = favoriteTracksDB.trackDao().getFavoriteTracks().map {
            TrackDbConverter.convertTrackEntityToTrack(it)
        }
        emit(tracks)
    }

    override fun getFavoriteTracksIds(): Flow<List<String>> = flow {
        val trackIds = favoriteTracksDB.trackDao().getFavoriteTracksIds()
        emit(trackIds)
    }

    override suspend fun deleteFavoriteTrack(track: TrackData) {
        favoriteTracksDB.trackDao().deleteFavoriteTrack(
            TrackDbConverter.convertTrackToTrackEntity(track)
        )
    }

    override suspend fun insertFavoriteTrack(track: TrackData) {
        favoriteTracksDB.trackDao().insertFavoriteTrack(
            TrackDbConverter.convertTrackToTrackEntity(track)
        )
    }
}
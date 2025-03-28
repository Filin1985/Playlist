package com.example.playlistmaker.data.media

import com.example.playlistmaker.data.convertes.DbConverter
import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val AppDB: AppDB) :
    FavoriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<TrackData>> = flow {
        val tracks = AppDB.trackDao().getFavoriteTracks().map {
            DbConverter.convertTrackEntityToTrack(it)
        }
        emit(tracks)
    }

    override fun getFavoriteTracksIds(): Flow<List<String>> = flow {
        val trackIds = AppDB.trackDao().getFavoriteTracksIds()
        emit(trackIds)
    }

    override suspend fun deleteFavoriteTrack(track: TrackData) {
        AppDB.trackDao().deleteFavoriteTrack(
            DbConverter.convertTrackToTrackEntity(track)
        )
    }

    override suspend fun insertFavoriteTrack(track: TrackData) {
        AppDB.trackDao().insertFavoriteTrack(
            DbConverter.convertTrackToTrackEntity(track)
        )
    }
}
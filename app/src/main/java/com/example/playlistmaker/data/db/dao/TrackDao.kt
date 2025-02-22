package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.search.model.TrackData

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteFavoriteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks")
    fun getFavoriteTracksIds(): List<String>
}
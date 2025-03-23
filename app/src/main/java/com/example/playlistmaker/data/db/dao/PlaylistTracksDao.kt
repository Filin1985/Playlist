package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDb(playlistTrackEntity: PlaylistTrackEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    fun removeTrackFromDb(playlistTrackEntity: PlaylistTrackEntity)
}

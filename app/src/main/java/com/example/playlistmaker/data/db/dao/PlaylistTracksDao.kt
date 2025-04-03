package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.data.entities.TrackEntity

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDb(playlistTrackEntity: PlaylistTrackEntity)

    @Delete(entity = PlaylistTrackEntity::class)
    fun removeTrackFromDb(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks")
    fun getPlaylistsTracks(): List<TrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    fun deleteTrackFromDB(trackId: String)
}

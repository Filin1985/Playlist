package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entities.PlaylistEntity

@Dao
interface PlaylistsDAO {
    @Insert(entity = PlaylistEntity::class)
    fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun showPlaylists(): List<PlaylistEntity>

    @Update(entity = PlaylistEntity::class)
    fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylist(playlistId: Int) : PlaylistEntity

    @Query("SELECT * FROM playlists")
    fun getPlaylistsInstantaneously(): List<PlaylistEntity>

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlistEntity: PlaylistEntity)
}
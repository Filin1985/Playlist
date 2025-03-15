package com.example.playlistmaker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_tracks")
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val previewUrl: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseYear: String,
    val country: String,
    val genre: String
)
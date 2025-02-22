package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
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

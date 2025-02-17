package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.utils.DateUtils

data class TrackDto (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) {
    fun getTrackTimeMillis(): String = DateUtils.msToMMSSFormat(trackTimeMillis)
}
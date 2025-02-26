package com.example.playlistmaker.data.convertes

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.search.model.TrackData

object TrackDbConverter {
    fun convertTrackToTrackEntity(track: TrackData): TrackEntity {
        return TrackEntity(
            trackId = track.trackId.toString(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName.orEmpty(),
            releaseYear = track.releaseDate,
            country = track.country,
            genre = track.primaryGenreName
        )
    }

    fun convertTrackEntityToTrack(trackEntity: TrackEntity): TrackData {
        return TrackData(
            trackId = trackEntity.trackId.toString(),
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            previewUrl = trackEntity.previewUrl,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseYear,
            country = trackEntity.country,
            primaryGenreName = trackEntity.genre
        )
    }
}
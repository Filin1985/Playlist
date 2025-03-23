package com.example.playlistmaker.ui.mapper

import com.example.playlistmaker.domain.search.model.TrackData

object TrackMapper {
    fun mapTrackToTrackRepresentation(track: TrackData) : TrackData {
        return TrackData(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            country = track.country,
            primaryGenreName = track.primaryGenreName
        )
    }
}
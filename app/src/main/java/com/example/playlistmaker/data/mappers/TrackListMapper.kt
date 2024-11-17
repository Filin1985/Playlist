package com.example.playlistmaker.data.mappers

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.TrackData


object TrackListMapper {
    private const val EMPTY_STRING = ""

    fun mapDtoToEntity(dtoTrackList: List<TrackDto>): List<TrackData> {
        val trackList = mutableListOf<TrackData>()
        for (dtoTrack in dtoTrackList) {
            val track = TrackData(
                dtoTrack.trackId ?: EMPTY_STRING,
                dtoTrack.trackName ?: EMPTY_STRING,
                dtoTrack.artistName ?: EMPTY_STRING,
                dtoTrack.trackTimeMillis ?: 0,
                dtoTrack.artworkUrl100 ?: EMPTY_STRING,
                dtoTrack.collectionName ?: EMPTY_STRING,
                dtoTrack.releaseDate ?: EMPTY_STRING,
                dtoTrack.primaryGenreName ?: EMPTY_STRING,
                dtoTrack.country ?: EMPTY_STRING,
                dtoTrack.previewUrl ?: EMPTY_STRING
            )
            trackList.add(track)
        }
        return trackList
    }

    fun mapEntityToDto(track: TrackData) = TrackDto(
        trackId = track.trackId ?: EMPTY_STRING,
        trackName = track.trackName ?: EMPTY_STRING,
        artistName = track.artistName ?: EMPTY_STRING,
        trackTimeMillis = track.trackTimeMillis ?: 0,
        artworkUrl100 = track.artworkUrl100 ?: EMPTY_STRING,
        collectionName = track.collectionName ?: EMPTY_STRING,
        releaseDate = track.releaseDate ?: EMPTY_STRING,
        primaryGenreName = track.primaryGenreName ?: EMPTY_STRING,
        country = track.country ?: EMPTY_STRING,
        previewUrl = track.previewUrl ?: EMPTY_STRING
    )
}
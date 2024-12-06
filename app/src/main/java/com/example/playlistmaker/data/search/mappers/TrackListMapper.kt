package com.example.playlistmaker.data.search.mappers

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.model.TrackData


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
}
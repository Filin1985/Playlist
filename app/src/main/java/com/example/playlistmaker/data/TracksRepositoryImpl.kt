package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.ui.tracks.SearchActivity.Companion.NOT_FOUND_CODE
import com.example.playlistmaker.utils.DateUtils

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTrack(text: String): List<TrackData> {
        val response = networkClient.doRequest(TracksSearchRequest(text))
        if (response.resultCode == 200) {
            return (response as TracksResponse).results.map {
                TrackData(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else if (response.resultCode == 404) {
            return response.resultCode
        } else {
            return emptyList()
        }
    }
}

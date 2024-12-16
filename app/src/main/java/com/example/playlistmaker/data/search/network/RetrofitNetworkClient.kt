package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.mappers.TrackListMapper
import com.example.playlistmaker.data.search.mappers.mapToApiResponse
import com.example.playlistmaker.domain.search.model.TrackData


class RetrofitNetworkClient(private val itunesService: ITunesAPI) : NetworkClient {

    override fun doRequest(dto: TracksSearchRequest): ResponseData<List<TrackData>> {
        val response = itunesService.search(dto.text).execute()
        return response.mapToApiResponse{ dto -> TrackListMapper.mapDtoToEntity(dto.results) }
    }

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}
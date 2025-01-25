package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.TracksResponse
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.mappers.TrackListMapper
import com.example.playlistmaker.data.search.mappers.mapToApiResponse
import com.example.playlistmaker.domain.search.model.TrackData


class RetrofitNetworkClient(private val itunesService: ITunesAPI) : NetworkClient {

    override fun doRequest(dto: TracksSearchRequest): ResponseData<List<TrackData>> {
        try {
            val response = itunesService.search(dto.text).execute()
            return when {
                response.code() == 404 -> ResponseData.EmptyResponse<List<TrackData>>()

                response.body() != null -> {
                    val responseBody = response.body() as TracksResponse
                    if(responseBody.results.isEmpty()) {
                        ResponseData.EmptyResponse<List<TrackData>>()
                    } else {
                        response.mapToApiResponse{ dto -> TrackListMapper.mapDtoToEntity(dto.results) }
                    }
                }

                else -> ResponseData.Error<List<TrackData>>()
            }
        } catch (exception: Exception) {
            return ResponseData.Error<List<TrackData>>()
        }
    }

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}
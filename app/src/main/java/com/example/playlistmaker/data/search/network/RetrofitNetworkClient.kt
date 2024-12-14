package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.search.model.ApiResponse
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.mappers.TrackListMapper
import com.example.playlistmaker.data.search.mappers.mapToApiResponse
import com.example.playlistmaker.domain.search.model.TrackData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    private val itunesService = retrofit.create(ITunesAPI::class.java)

    override fun doRequest(dto: TracksSearchRequest): ApiResponse<List<TrackData>> {
        val response = itunesService.search(dto.text).execute()
        return response.mapToApiResponse{ dto -> TrackListMapper.mapDtoToEntity(dto.results) }
    }
}
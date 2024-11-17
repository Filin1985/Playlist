package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.mappers.TrackListMapper
import com.example.playlistmaker.data.mappers.mapToApiResponse
import com.example.playlistmaker.domain.models.TrackData
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
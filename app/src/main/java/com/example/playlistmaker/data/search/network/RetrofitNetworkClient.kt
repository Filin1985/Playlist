package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.TracksResponse
import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.mappers.TrackListMapper
import com.example.playlistmaker.data.search.mappers.mapToApiResponse
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class RetrofitNetworkClient(private val itunesService: ITunesAPI) : NetworkClient {

    override suspend fun doRequest(dto: TracksSearchRequest): Flow<ResponseData<List<TrackData>>> = flow {
        try {
            val response = itunesService.search(dto.text)
            when {
                response.code() == 404 -> emit(ResponseData.EmptyResponse<List<TrackData>>())

                response.body() != null -> {
                    val responseBody = response.body() as TracksResponse
                    if (responseBody.results.isEmpty()) {
                        emit(ResponseData.EmptyResponse<List<TrackData>>())
                    } else {
                        emit(response.mapToApiResponse { dto -> TrackListMapper.mapDtoToEntity(dto.results) })
                    }
                }

                else -> emit(ResponseData.Error<List<TrackData>>())
            }
        } catch (exception: Exception) {
            emit(ResponseData.Error<List<TrackData>>())
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}
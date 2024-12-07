package com.example.playlistmaker.data.mappers

import com.example.playlistmaker.data.dto.ApiResponse
import retrofit2.Response

fun <T, V> Response<T>.mapToApiResponse(mapSuccess: (dto: T) -> V):ApiResponse<V> {
    val dto = body()
    val errorMessage = "Loading Error"
    return when {
        !isSuccessful -> {
            ApiResponse.Error(errorBody()?.string() ?: errorMessage)
        }
        dto != null -> ApiResponse.Success(mapSuccess(dto))
        else -> {
            ApiResponse.Error(errorMessage)
        }
    }
}
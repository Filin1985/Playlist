package com.example.playlistmaker.data.search.mappers

import com.example.playlistmaker.domain.search.model.ResponseData
import retrofit2.Response

fun <T, V> Response<T>.mapToApiResponse(mapSuccess: (dto: T) -> V): ResponseData<V> {
    val dto = body()
    val errorMessage = "Loading Error"
    return when {
        !isSuccessful -> {
            ResponseData.Error(errorBody()?.string() ?: errorMessage)
        }
        dto != null -> ResponseData.Success(mapSuccess(dto))
        else -> {
            ResponseData.Error(errorMessage)
        }
    }
}
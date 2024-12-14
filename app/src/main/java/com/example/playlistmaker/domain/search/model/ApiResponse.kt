package com.example.playlistmaker.domain.search.model

sealed interface ApiResponse<T> {
    data class Success<T> (val data: T): ApiResponse<T>
    data class Error<T> (val message: String): ApiResponse<T>
    class EmptyResponse<T>() : ApiResponse<T>
}
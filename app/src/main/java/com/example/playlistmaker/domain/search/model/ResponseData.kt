package com.example.playlistmaker.domain.search.model

sealed interface ResponseData<T> {
    data class Success<T> (val data: T): ResponseData<T>
    data class Error<T> (val message: String): ResponseData<T>
    class EmptyResponse<T>() : ResponseData<T>
}
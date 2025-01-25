package com.example.playlistmaker.domain.search.model

sealed interface ResponseData<T> {
    data class Success<T> (val data: T): ResponseData<T>
    class Error<T> (): ResponseData<T>
    class EmptyResponse<T>() : ResponseData<T>
}
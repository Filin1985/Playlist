package com.example.playlistmaker.domain.models

sealed interface TracksConsumer<T> {
    data class Data<T>(val value: T) : TracksConsumer<T>
    class EmptyData<T>() : TracksConsumer<T>
    data class Error<T>(val message: String) : TracksConsumer<T>
}
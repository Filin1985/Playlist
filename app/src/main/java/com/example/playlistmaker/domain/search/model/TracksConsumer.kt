package com.example.playlistmaker.domain.search.model

sealed interface TracksConsumer<T> {
    data class Data<T>(val value: T) : TracksConsumer<T>
    class EmptyData<T>() : TracksConsumer<T>
    class Error<T>() : TracksConsumer<T>
}
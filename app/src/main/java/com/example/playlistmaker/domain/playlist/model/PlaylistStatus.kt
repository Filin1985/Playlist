package com.example.playlistmaker.domain.playlist.model

sealed interface PlaylistStatus<T> {
    data class Content<T>(val data: T) : PlaylistStatus<T>
    class Empty<T> : PlaylistStatus<T>
}
package com.example.playlistmaker.domain.consumer

import com.example.playlistmaker.domain.search.model.TracksConsumer

interface Consumer<T> {
    fun consume(data: TracksConsumer<T>)
}
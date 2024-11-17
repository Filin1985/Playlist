package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.TracksConsumer

interface Consumer<T> {
    fun consume(data: TracksConsumer<T>)
}
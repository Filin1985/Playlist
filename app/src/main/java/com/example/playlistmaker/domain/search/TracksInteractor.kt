package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.model.TrackData

interface TracksInteractor {
    fun execute(text: String, consumer: Consumer<List<TrackData>>)
}
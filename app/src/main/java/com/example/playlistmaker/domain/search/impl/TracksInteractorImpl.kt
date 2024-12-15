package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.search.model.TracksConsumer
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun execute(
        text: String,
        consumer: Consumer<List<TrackData>>
    ) {
        executor.execute {
            val trackResponse = repository.searchTracks(text)
            when(trackResponse){
                is ResponseData.Error -> consumer.consume(TracksConsumer.Error(trackResponse.message))
                is ResponseData.Success -> consumer.consume(TracksConsumer.Data(trackResponse.data))
                is ResponseData.EmptyResponse -> consumer.consume(TracksConsumer.EmptyData())
            }
        }
    }
}
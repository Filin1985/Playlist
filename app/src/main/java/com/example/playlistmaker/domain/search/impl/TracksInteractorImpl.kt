package com.example.playlistmaker.domain.search.impl

import android.util.Log
import com.example.playlistmaker.data.search.dto.ApiResponse
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.search.model.TracksConsumer
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        text: String,
        consumer: Consumer<List<TrackData>>
    ) {
        executor.execute {
            val trackResponse = repository.searchTracks(text)
            Log.d("trackResponse", "trackResponse------ $trackResponse")
            when(trackResponse){
                is ApiResponse.Error -> consumer.consume(TracksConsumer.Error(trackResponse.message))
                is ApiResponse.Success -> consumer.consume(TracksConsumer.Data(trackResponse.data))
                is ApiResponse.EmptyResponse -> consumer.consume(TracksConsumer.EmptyData())
            }
        }
    }
}
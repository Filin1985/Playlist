package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.data.dto.ApiResponse
import com.example.playlistmaker.domain.Consumer
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.domain.models.TracksConsumer
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
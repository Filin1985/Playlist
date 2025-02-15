package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.model.ResponseData
import com.example.playlistmaker.domain.consumer.Consumer
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.domain.search.model.TracksConsumer
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun execute(text: String): Flow<ResponseData<List<TrackData>>> {
        return repository.searchTracks(text)
    }
}
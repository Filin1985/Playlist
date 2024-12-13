package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class AddTracksToHistoryListImlp(private val searchTrackList: SearchHistoryRepository):
    AddTracksHistoryListUseCase {
    override fun execute(track: TrackData) {
        searchTrackList.addTrackToList(track)
    }
}
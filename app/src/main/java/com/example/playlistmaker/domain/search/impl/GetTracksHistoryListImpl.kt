package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class GetTracksHistoryListImpl(private val searchTrackList: SearchHistoryRepository) :
    GetTracksHistoryListUseCase {
    override fun execute(): ArrayList<TrackData> {
        return searchTrackList.getTrackList()
    }
}
package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistory
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class GetTracksHistoryListImpl(private val searchTrackList: SearchHistory) :
    GetTracksHistoryListUseCase {
    override fun execute(): ArrayList<TrackData> {
        return searchTrackList.getTrackList()
    }
}
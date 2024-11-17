package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.domain.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.models.TrackData

class GetTracksHistoryListImpl(private val searchTrackList: SearchHistory) :
    GetTracksHistoryListUseCase {
    override fun execute(): ArrayList<TrackData> {
        return searchTrackList.getTrackList()
    }
}
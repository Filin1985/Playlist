package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistory
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase

class ClearTracksHistoryListImpl(private val searchTrackList: SearchHistory) :
    ClearTracksHistoryListUseCase {
    override fun execute() {
        searchTrackList.clearTrackList()
    }
}
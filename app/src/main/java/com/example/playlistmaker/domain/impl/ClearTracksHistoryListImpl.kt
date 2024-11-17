package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.domain.interfaces.ClearTracksHistoryListUseCase

class ClearTracksHistoryListImpl(private val searchTrackList: SearchHistory) :
    ClearTracksHistoryListUseCase {
    override fun execute() {
        searchTrackList.clearTrackList()
    }
}
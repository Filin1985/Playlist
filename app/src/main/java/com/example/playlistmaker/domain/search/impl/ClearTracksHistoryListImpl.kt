package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase

class ClearTracksHistoryListImpl(private val searchTrackList: SearchHistoryRepository) :
    ClearTracksHistoryListUseCase {
    override fun execute() {
        searchTrackList.clearTrackList()
    }
}
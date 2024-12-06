package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchHistory
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.model.TrackData

class AddTracksToHistoryListImlp(private val searchTrackList: SearchHistory):
    AddTracksHistoryListUseCase {
    override fun execute(track: TrackData) {
        searchTrackList.addTrackToList(track)
    }
}
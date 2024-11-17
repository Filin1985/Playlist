package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.domain.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.models.TrackData

class AddTracksToHistoryListImlp(private val searchTrackList: SearchHistory): AddTracksHistoryListUseCase {
    override fun execute(track: TrackData) {
        searchTrackList.addTrackToList(track)
    }
}
package com.example.playlistmaker.creator

import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.impl.AddTracksToHistoryListImlp
import com.example.playlistmaker.domain.search.impl.ClearTracksHistoryListImpl
import com.example.playlistmaker.domain.search.impl.GetTracksHistoryListImpl
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase

class SearchCreator(private val searchHistoryRepository: SearchHistoryRepository) {
    fun getSearchHistoryStorage(): GetTracksHistoryListUseCase {
        return GetTracksHistoryListImpl(searchHistoryRepository)
    }

    fun addSearchHistoryStorage(): AddTracksHistoryListUseCase {
        return AddTracksToHistoryListImlp(searchHistoryRepository)
    }

    fun clearSearchHistoryStorage(): ClearTracksHistoryListUseCase {
        return ClearTracksHistoryListImpl(searchHistoryRepository)
    }
}
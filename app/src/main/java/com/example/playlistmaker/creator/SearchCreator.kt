package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.SearchHistory
import com.example.playlistmaker.data.search.impl.SearchHistoryImpl
import com.example.playlistmaker.domain.search.impl.AddTracksToHistoryListImlp
import com.example.playlistmaker.domain.search.impl.ClearTracksHistoryListImpl
import com.example.playlistmaker.domain.search.impl.GetTracksHistoryListImpl
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase

class SearchCreator {
    fun getSearchHistoryStorage(sharedPreferences: SharedPreferences): GetTracksHistoryListUseCase {
        return GetTracksHistoryListImpl(provideHistoryTrackList(sharedPreferences))
    }

    fun addSearchHistoryStorage(sharedPreferences: SharedPreferences): AddTracksHistoryListUseCase {
        return AddTracksToHistoryListImlp(provideHistoryTrackList(sharedPreferences))
    }

    fun clearSearchHistoryStorage(sharedPreferences: SharedPreferences): ClearTracksHistoryListUseCase {
        return ClearTracksHistoryListImpl(provideHistoryTrackList(sharedPreferences))
    }

    private fun provideHistoryTrackList(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPreferences)
    }
}
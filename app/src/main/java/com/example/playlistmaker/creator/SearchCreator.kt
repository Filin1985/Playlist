package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.repository.SearchHistoryImpl
import com.example.playlistmaker.domain.impl.AddTracksToHistoryListImlp
import com.example.playlistmaker.domain.impl.ClearTracksHistoryListImpl
import com.example.playlistmaker.domain.impl.GetTracksHistoryListImpl
import com.example.playlistmaker.domain.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.interfaces.GetTracksHistoryListUseCase

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
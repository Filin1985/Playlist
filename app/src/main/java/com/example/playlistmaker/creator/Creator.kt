package com.example.playlistmaker.creator

import android.content.SharedPreferences
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.repository.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.data.repository.SearchHistoryImpl
import com.example.playlistmaker.domain.impl.AddTracksToHistoryListImlp
import com.example.playlistmaker.domain.impl.ClearTracksHistoryListImpl
import com.example.playlistmaker.domain.impl.GetTracksHistoryListImpl
import com.example.playlistmaker.domain.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.interfaces.GetTracksHistoryListUseCase

object Creator {
    fun provideSearchTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(provideSearchRepository())
    }

    fun getSearchHistoryStorage(sharedPreferences: SharedPreferences): GetTracksHistoryListUseCase {
        return GetTracksHistoryListImpl(provideHistoryTrackList(sharedPreferences))
    }

    fun addSearchHistoryStorage(sharedPreferences: SharedPreferences): AddTracksHistoryListUseCase {
        return AddTracksToHistoryListImlp(provideHistoryTrackList(sharedPreferences))
    }

    fun clearSearchHistoryStorage(sharedPreferences: SharedPreferences): ClearTracksHistoryListUseCase {
        return ClearTracksHistoryListImpl(provideHistoryTrackList(sharedPreferences))
    }

    private fun getRetrofitNetworkClient() = RetrofitNetworkClient()

    private fun provideSearchRepository() =
        TracksRepositoryImpl(getRetrofitNetworkClient())

    private fun provideHistoryTrackList(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPreferences)
    }
}
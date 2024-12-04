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
import com.example.playlistmaker.domain.models.TrackData

object Creator {
    fun provideSearchTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(provideSearchRepository())
    }

    fun getPlayerCreator(track: TrackData): PlayerCreator {
        return PlayerCreator(track)
    }

    fun getSearchCreator(): SearchCreator {
        return SearchCreator()
    }

    private fun getRetrofitNetworkClient() = RetrofitNetworkClient()

    private fun provideSearchRepository() =
        TracksRepositoryImpl(getRetrofitNetworkClient())

}
package com.example.playlistmaker.di

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesAPI
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import org.koin.dsl.module

val viewModuleModule = module {
    single<ThemeViewModel> {
        ThemeViewModel(get(), get(), get())
    }

    single<SettingsViewModel> {
        SettingsViewModel(get(), get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }

    single<SearchViewModel> {
        SearchViewModel(get(), get(), get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }
}
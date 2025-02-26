package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.FavoriteTracksDB
import com.example.playlistmaker.data.db.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesAPI
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SharedPrefThemeRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.favorites.FavoriteTracksRepository
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharedPrefRepository
import com.example.playlistmaker.domain.sharing.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SharedPrefThemeRepositoryImpl.DARK_THEME,
            Context.MODE_PRIVATE
        )
    }

    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    single<SharedPrefRepository> {
        SharedPrefThemeRepositoryImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SearchHistoryRepositoryImpl.SEARCH_HISTORY,
            Context.MODE_PRIVATE
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory {
        MediaPlayer()
    }

    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), FavoriteTracksDB::class.java, "database.db")
            .build()
    }
}
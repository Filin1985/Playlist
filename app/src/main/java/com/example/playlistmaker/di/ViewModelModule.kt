package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.player.view_model.PlayerVewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModuleModule = module {
    viewModel<ThemeViewModel> {
        ThemeViewModel(get(), get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get(), get())
    }

    viewModel<PlayerVewModel> { params ->
        PlayerVewModel(
            track = params.get(),
            preparePlayer = get(),
            destroyPlayer = get(),
            pausePlayer = get(),
            playbackPlayer = get(),
            playTrackPlayer = get(),
            getPlayerTime = get(),
            getPlayerState = get(),
            setCompletionPlayer = get()
        )
    }
}
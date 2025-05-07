package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModel
import com.example.playlistmaker.ui.media.playlist.view_model.MediaNewPlaylistViewModel
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModel
import com.example.playlistmaker.ui.media.playlist.view_model.PlaylistEditViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerVewModel
import com.example.playlistmaker.ui.playlistDetails.view_model.DetailPlaylistViewModel
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

    viewModel<PlayerVewModel> { (track: String) ->
        PlayerVewModel(
            track = track,
            getPlayerTime = get(),
            getPlayerState = get(),
            deleteTrackFromFavorite = get(),
            insertTrackToFavorite = get(),
            setCompletionPlayer = get(),
            getTrackIdsFromDb = get(),
            showPlaylistsUseCase = get(),
            addTrackToPlaylistUseCase = get(),
            updatePlaylistUseCase = get()
        )
    }

    viewModel<MediaFavouriteViewModel> {
        MediaFavouriteViewModel(get())
    }

    viewModel<MediaPlaylistViewModel> {
        MediaPlaylistViewModel(showPlaylistsUseCase = get())
    }

    viewModel<MediaNewPlaylistViewModel> {
        MediaNewPlaylistViewModel(createNewPlaylistUseCase = get())
    }

    viewModel<DetailPlaylistViewModel>() { (playlistId: Int) ->
        DetailPlaylistViewModel(
            playlistId = playlistId,
            getPlaylistByIdUseCase = get(),
            getTracksFromPlaylistUseCase = get(),
            deleteTrackFromPlaylistUseCase = get(),
            sharePlaylistUseCase = get(),
            deletePlaylistUseCase = get(),
        )
    }

    viewModel<PlaylistEditViewModel>() { (playlistId: Int) ->
        PlaylistEditViewModel(
            playlistId = playlistId,
            getPlaylistByIdUseCase = get(),
            updatePlaylistUseCase = get(),
        )
    }
}
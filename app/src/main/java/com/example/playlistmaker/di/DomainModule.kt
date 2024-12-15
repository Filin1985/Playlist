package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.impl.CompletionUseCaseImpl
import com.example.playlistmaker.domain.player.impl.DestroyPlayerUseCaseImpl
import com.example.playlistmaker.domain.player.impl.GetCurrentPlayerTrackTimeUseCaseImpl
import com.example.playlistmaker.domain.player.impl.GetPlayerStateUseCaseImpl
import com.example.playlistmaker.domain.player.impl.PauseTrackUseCaseImpl
import com.example.playlistmaker.domain.player.impl.PlayTrackUseCaseImpl
import com.example.playlistmaker.domain.player.impl.PlaybackTrackUseCaseImpl
import com.example.playlistmaker.domain.player.impl.PreparePlayerUseCaseImpl
import com.example.playlistmaker.domain.player.interfaces.CompletionUseCase
import com.example.playlistmaker.domain.player.interfaces.DestroyPlayerUseCase
import com.example.playlistmaker.domain.player.interfaces.GetCurrentPlayerTrackTimeUseCase
import com.example.playlistmaker.domain.player.interfaces.GetPlayerStateUseCase
import com.example.playlistmaker.domain.player.interfaces.PauseTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlayTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PlaybackTrackUseCase
import com.example.playlistmaker.domain.player.interfaces.PreparePlayerUseCase
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.AddTracksToHistoryListImlp
import com.example.playlistmaker.domain.search.impl.ClearTracksHistoryListImpl
import com.example.playlistmaker.domain.search.impl.GetTracksHistoryListImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.search.interfaces.AddTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.ClearTracksHistoryListUseCase
import com.example.playlistmaker.domain.search.interfaces.GetTracksHistoryListUseCase
import com.example.playlistmaker.domain.settings.impl.GetPrevThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SaveNewThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.settings.interfaces.GetPrevThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SaveNewThemeUseCase
import com.example.playlistmaker.domain.settings.interfaces.SwitchThemeUseCase
import com.example.playlistmaker.domain.sharing.impl.MailToSupportUseCaseImpl
import com.example.playlistmaker.domain.sharing.impl.OpenTermsUseCaseImpl
import com.example.playlistmaker.domain.sharing.impl.SharingAppUseCaseImpl
import com.example.playlistmaker.domain.sharing.interfaces.MailToSupportUseCase
import com.example.playlistmaker.domain.sharing.interfaces.OpenTermsUseCase
import com.example.playlistmaker.domain.sharing.interfaces.SharingAppUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<SaveNewThemeUseCase> {
        SaveNewThemeUseCaseImpl(get())
    }

    factory<GetPrevThemeUseCase> {
        GetPrevThemeUseCaseImpl(get())
    }

    factory<SwitchThemeUseCase> {
        SwitchThemeUseCaseImpl(get())
    }

    factory<SharingAppUseCase> {
        SharingAppUseCaseImpl(get())
    }

    factory<MailToSupportUseCase> {
        MailToSupportUseCaseImpl(get())
    }

    factory<OpenTermsUseCase> {
        OpenTermsUseCaseImpl(get())
    }

    factory<GetTracksHistoryListUseCase> {
        GetTracksHistoryListImpl(get())
    }

    factory<AddTracksHistoryListUseCase> {
        AddTracksToHistoryListImlp(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<ClearTracksHistoryListUseCase> {
        ClearTracksHistoryListImpl(get())
    }

    factory<GetPlayerStateUseCase> {
        GetPlayerStateUseCaseImpl(get())
    }

    factory<DestroyPlayerUseCase> {
        DestroyPlayerUseCaseImpl(get())
    }

    factory<GetCurrentPlayerTrackTimeUseCase> {
        GetCurrentPlayerTrackTimeUseCaseImpl(get())
    }

    factory<PauseTrackUseCase> {
        PauseTrackUseCaseImpl(get())
    }

    factory<PlaybackTrackUseCase> {
        PlaybackTrackUseCaseImpl(get())
    }

    factory<PlayTrackUseCase> {
        PlayTrackUseCaseImpl(get())
    }

    factory<PreparePlayerUseCase> {
        PreparePlayerUseCaseImpl(get())
    }

    factory<CompletionUseCase> {
        CompletionUseCaseImpl(get())
    }
}
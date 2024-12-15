package com.example.playlistmaker.di

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
    single<SaveNewThemeUseCase> {
        SaveNewThemeUseCaseImpl(get())
    }

    single<GetPrevThemeUseCase> {
        GetPrevThemeUseCaseImpl(get())
    }

    single<SwitchThemeUseCase> {
        SwitchThemeUseCaseImpl(get())
    }

    single<SharingAppUseCase> {
        SharingAppUseCaseImpl(get())
    }

    single<MailToSupportUseCase> {
        MailToSupportUseCaseImpl(get())
    }

    single<OpenTermsUseCase> {
        OpenTermsUseCaseImpl(get())
    }



    single<GetTracksHistoryListUseCase> {
        GetTracksHistoryListImpl(get())
    }

    single<AddTracksHistoryListUseCase> {
        AddTracksToHistoryListImlp(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<ClearTracksHistoryListUseCase> {
        ClearTracksHistoryListImpl(get())
    }
}
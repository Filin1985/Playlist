package com.example.playlistmaker.creator

import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
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
import com.example.playlistmaker.domain.search.model.TrackData

class PlayerCreator(val track: TrackData) {
    private val mediaPlayer = MediaPlayerRepositoryImpl(track)

    fun provideGetPlayerStateUseCase(): GetPlayerStateUseCase {
        return GetPlayerStateUseCaseImpl(mediaPlayer)
    }

    fun providePreparePlayerUseCase(): PreparePlayerUseCase {
        return PreparePlayerUseCaseImpl(mediaPlayer)
    }

    fun providePlayTrackUseCase(): PlayTrackUseCase {
        return PlayTrackUseCaseImpl(mediaPlayer)
    }

    fun providePauseTrackUseCase(): PauseTrackUseCase {
        return PauseTrackUseCaseImpl(mediaPlayer)
    }

    fun provideDestroyPlayerUseCase(): DestroyPlayerUseCase {
        return DestroyPlayerUseCaseImpl(mediaPlayer)
    }

    fun providePlayTrackTimeUseCase() : GetCurrentPlayerTrackTimeUseCase {
        return GetCurrentPlayerTrackTimeUseCaseImpl(mediaPlayer)
    }

    fun providePlaybackTrackUseCase(): PlaybackTrackUseCase {
        return PlaybackTrackUseCaseImpl(mediaPlayer)
    }

    fun provideCompletionUseCase():CompletionUseCase {
        return CompletionUseCaseImpl(mediaPlayer)
    }
}
package com.example.playlistmaker.creator

import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.domain.impl.player.DestroyPlayerUseCaseImpl
import com.example.playlistmaker.domain.impl.player.GetCurrentPlayerTrackTimeUseCaseImpl
import com.example.playlistmaker.domain.impl.player.GetPlayerStateUseCaseImpl
import com.example.playlistmaker.domain.impl.player.PauseTrackUseCaseImpl
import com.example.playlistmaker.domain.impl.player.PlayTrackUseCaseImpl
import com.example.playlistmaker.domain.impl.player.PlaybackTrackUseCaseImpl
import com.example.playlistmaker.domain.impl.player.PreparePlayerUseCaseImpl
import com.example.playlistmaker.domain.interfaces.player.DestroyPlayerUseCase
import com.example.playlistmaker.domain.interfaces.player.GetCurrentPlayerTrackTimeUseCase
import com.example.playlistmaker.domain.interfaces.player.GetPlayerStateUseCase
import com.example.playlistmaker.domain.interfaces.player.PauseTrackUseCase
import com.example.playlistmaker.domain.interfaces.player.PlayTrackUseCase
import com.example.playlistmaker.domain.interfaces.player.PlaybackTrackUseCase
import com.example.playlistmaker.domain.interfaces.player.PreparePlayerUseCase
import com.example.playlistmaker.domain.models.TrackData

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
}
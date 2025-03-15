package com.example.playlistmaker.domain.mediateca.playlists

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: Playlist)

    fun showPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist, track: TrackData)

    suspend fun addTrackToPlaylistsTracksStorage(track: TrackData)
}
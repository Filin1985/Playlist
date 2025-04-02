package com.example.playlistmaker.domain.mediateca.playlists

import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: Playlist)

    fun showPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist, track: TrackData)

    suspend fun addTrackToPlaylistsStorage(track: TrackData)

    suspend fun getPlaylistById(playlistId: Int): Playlist

    fun getTracksFromPlaylist(tracksId: List<String>): Flow<List<TrackData>>

    suspend fun deleteTrackFromPlaylist(track: TrackData, playlist: Playlist)
}
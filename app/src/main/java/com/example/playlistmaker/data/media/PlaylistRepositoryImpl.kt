package com.example.playlistmaker.data.mediateca

import com.example.playlistmaker.data.convertes.DbConverter
import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDB: AppDB
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        appDB.playlistsDAO().addPlaylist(
            DbConverter.convertPlaylistToPlaylistEntity(playlist)
        )
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: TrackData) {

        val updatedPlaylistTracksId = playlist.tracksId.toMutableList().also {
            it.add(track.trackId)
        }

        val playlistWithUpdatedTracksId = playlist.copy(
            tracksId = updatedPlaylistTracksId
        )
        appDB.playlistsDAO().updatePlaylist(
            DbConverter.convertPlaylistToPlaylistEntity(playlistWithUpdatedTracksId)
        )
    }

    override fun showPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDB.playlistsDAO().showPlaylists().map {
            DbConverter.convertPlaylistEntityToPlaylist(it)
        }
        emit(playlists)
    }

    override suspend fun addTrackToPlaylistsStorage(track: TrackData) {
        appDB.playlistTracksDao().addTrackToDb(
            DbConverter.convertTrackToPlaylistTrackEntity(track)
        )
    }
}
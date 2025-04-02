package com.example.playlistmaker.data.mediateca

import androidx.core.net.toUri
import com.example.playlistmaker.data.convertes.DbConverter
import com.example.playlistmaker.data.db.AppDB
import com.example.playlistmaker.domain.mediateca.playlists.PlaylistsRepository
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

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

    override suspend fun updatePlaylist(newPlaylist: Playlist) {
        appDB.playlistsDAO().updatePlaylist(
            DbConverter.convertPlaylistToPlaylistEntity(newPlaylist)
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

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return DbConverter.convertPlaylistEntityToPlaylist(
            appDB.playlistsDAO().getPlaylist(playlistId)
        )
    }

    override fun getTracksFromPlaylist(tracksId: List<String>): Flow<List<TrackData>> = flow {
        val tracks = appDB.playlistTracksDao().getPlaylistsTracks().map {
            DbConverter.convertTrackEntityToTrack(it)
        }.filter { tracksId.contains(it.trackId)  }

        emit(tracks)
    }

    override suspend fun deleteTrackFromPlaylist(track: TrackData, playlist: Playlist) {
        deleteTrackFromPlaylists(track, playlist)
        updatePlaylist(playlist, track)
    }

    private suspend fun deleteTrackFromPlaylists(track: TrackData, playlistWithDeletedTrack: Playlist) {
        deleteTrackFromPlaylistsTracks(track.trackId, playlistWithDeletedTrack)
    }

    private suspend fun deleteTrackFromPlaylistsTracks(trackId: String, playlistWithDeletedTrack: Playlist) {

        val playlists = appDB.playlistsDAO().getPlaylistsInstantaneously().map { DbConverter.convertPlaylistEntityToPlaylist(it) }
        var somePlaylistsContainTrack = false

        playlists.forEach {
            if (it.tracksId.contains(trackId) && it.id != playlistWithDeletedTrack.id) {
                somePlaylistsContainTrack = true
            }
        }

        if (!somePlaylistsContainTrack) {
            appDB.playlistTracksDao().deleteTrackFromDB(trackId)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDB.playlistsDAO().deletePlaylist(
            DbConverter.convertPlaylistToPlaylistEntity(playlist)
        )
        playlist.tracksId.forEach {
            deleteTrackFromPlaylistsTracks(it, playlist)
        }
        playlist.coverUri?.let {
            it.toString().toUri().path?.let { File(it).delete() }
        }
    }
}
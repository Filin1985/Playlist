package com.example.playlistmaker.data.convertes

import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.data.entities.TrackEntity
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.TrackData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DbConverter {
    fun convertTrackToTrackEntity(track: TrackData): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            country = track.country,
            primaryGenreName = track.primaryGenreName
        )
    }

    fun convertTrackEntityToTrack(trackEntity: TrackEntity): TrackData {
        return TrackData(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            previewUrl = trackEntity.previewUrl,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            country = trackEntity.country,
            primaryGenreName = trackEntity.primaryGenreName
        )
    }

    fun convertTrackToPlaylistTrackEntity(track: TrackData): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            country = track.country,
            primaryGenreName = track.primaryGenreName
        )
    }

    fun convertPlaylistTrackEntityToTrack(playlistTrackEntity: PlaylistTrackEntity): TrackData {
        return TrackData(
            trackId = playlistTrackEntity.trackId,
            trackName = playlistTrackEntity.trackName,
            artistName = playlistTrackEntity.artistName,
            trackTimeMillis = playlistTrackEntity.trackTimeMillis,
            previewUrl = playlistTrackEntity.previewUrl,
            artworkUrl100 = playlistTrackEntity.artworkUrl100,
            collectionName = playlistTrackEntity.collectionName,
            releaseDate = playlistTrackEntity.releaseDate,
            country = playlistTrackEntity.country,
            primaryGenreName = playlistTrackEntity.primaryGenreName
        )
    }

    fun convertPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description.orEmpty(),
            coverUri = playlist.coverUri?.toString().orEmpty(),
            tracksId = Json.encodeToString(playlist.tracksId),
            size = playlist.size
        ).also {
            if (playlist.id != -1) it.id = playlist.id
        }
    }

    fun convertPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        Log.d("PlaylistEntity---------", "${playlistEntity}")
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            coverUri = playlistEntity.coverUri.toUri(),
            tracksId = Json.decodeFromString(playlistEntity.tracksId),
        )
    }
}
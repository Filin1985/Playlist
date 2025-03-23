package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.player.model.TrackPlaylistState
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.player.view_model.PlayerVewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: TrackData
    private val viewModel: PlayerVewModel by viewModel { parametersOf(track) }
    private lateinit var adapter: PlayerBottomSheetAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        track = Gson().fromJson(intent.getStringExtra(SearchFragment.TRACK), TrackData::class.java)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).also {
            it.state = BottomSheetBehavior.STATE_HIDDEN

            it.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }
                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) { }
            })
        }

        adapter = PlayerBottomSheetAdapter(viewModel.playlistLiveData.value!!)
        adapter.listener = {
            viewModel.addTrackToPlaylist(it)
            showTrackAddingToPlaylistResult(viewModel.playlistStateLiveData.value!!, it)
        }
        binding.recyclerViewPlaylists.adapter = adapter
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.arrowBackPlayer.setOnClickListener {
            finish()
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.showPlaylists()
            binding.recyclerViewPlaylists.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.playlistLiveData.observe(this) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.stateLiveData.observe(this) {
            stateRender(it)
        }

        viewModel.playTrackProgressLiveData.observe(this) {
            playTimeRender(it)
        }

        viewModel.isTrackInFavoriteLiveData.observe(this) {
            toggleFavoriteTrack(it)
        }

        val albumCover = findViewById<ImageView>(R.id.album_cover)
        Glide.with(albumCover)
            .load(track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(albumCover.resources.getDimensionPixelSize(R.dimen.art_work)))
            .into(albumCover)

        binding.playerSongName.text = track.trackName

        binding.playerBand.text = track.artistName

        if (track.collectionName.isEmpty()) {
            binding.albumCover.visibility = View.GONE
        } else binding.playerAlbumData.text = track.collectionName

        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val year = formatDate?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
        binding.playerYearData.text = year

        binding.playerGenreData.text = track.primaryGenreName
        binding.playerCountryData.text = track.country

        binding.playerPlay.setOnClickListener {
            viewModel.playControl()
            viewModel.startTimer()
        }

        binding.playerLike.setOnClickListener {
            viewModel.toggleTrackFavoriteState()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun stateRender(playerState: MediaPlayerState) {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> binding.playerPlay.setImageResource(R.drawable.ic_player_pause)
            MediaPlayerState.STATE_PAUSED -> binding.playerPlay.setImageResource(R.drawable.ic_player_play)
            MediaPlayerState.STATE_PREPARED -> {
                binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                binding.playerPlay.setImageResource(R.drawable.ic_player_play)
            }

            MediaPlayerState.STATE_DEFAULT -> {}
        }
    }

    private fun playTimeRender(time: Int) {
        when (viewModel.stateLiveData.value) {
            MediaPlayerState.STATE_PLAYING, MediaPlayerState.STATE_PAUSED -> {
                binding.playerTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
            }

            MediaPlayerState.STATE_PREPARED -> {
                binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            MediaPlayerState.STATE_DEFAULT, null -> {}
        }
    }

    private fun toggleFavoriteTrack(isTrackInFavorite: Boolean?) {
        val likeImage = if(isTrackInFavorite == true) R.drawable.ic_player_like_active else R.drawable.ic_player_like
        binding.playerLike.setImageDrawable(getDrawable(likeImage))
    }

    private fun showTrackAddingToPlaylistResult(trackPlaylistRelationship: TrackPlaylistState, playlist: Playlist) {
        if (trackPlaylistRelationship == TrackPlaylistState.TRACK_IS_ALREADY_ADDED) {
            Toast.makeText(
                this,
                resources.getString(R.string.track_is_already_in_playlist).format(playlist.title),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.track_is_added_to_playlist).format(playlist.title),
                Toast.LENGTH_SHORT
            ).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    companion object {
        const val TRACK = "TRACK"

        fun createArgs(encodedTrack: String) : Bundle {
            return bundleOf(TRACK to encodedTrack)
        }
    }
}

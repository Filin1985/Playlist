package com.example.playlistmaker.ui.player.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.ui.search.activity.SearchActivity.Companion.TRACK
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.ui.player.view_model.PlayerVewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var viewModel: PlayerVewModel? = null

    private lateinit var track: TrackData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Gson().fromJson(intent.getStringExtra(TRACK), TrackData::class.java)
        binding.arrowBackPlayer.setOnClickListener {
            finish()
        }

        viewModel =
            ViewModelProvider(this, PlayerVewModel.factory(track))[PlayerVewModel::class.java]
        viewModel?.stateLiveData?.observe(this) {
            stateRender(it)
        }

        viewModel?.playTrackProgressLiveData?.observe(this) {
            playTimeRender(it)
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

        binding.playerControl.setOnClickListener {
            viewModel?.playControl()
            viewModel?.startUpdaterRunnable()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel?.pausePlayer()
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }

    private fun stateRender(playerState: MediaPlayerState) {
        when (playerState) {
            MediaPlayerState.STATE_PLAYING -> binding.playerControl.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_pause_white else R.drawable.ic_player_pause)
            MediaPlayerState.STATE_PAUSED -> binding.playerControl.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_play_white else R.drawable.ic_player_play)
            MediaPlayerState.STATE_PREPARED -> {
                binding.playerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                binding.playerControl.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_play_white else R.drawable.ic_player_play)
            }

            MediaPlayerState.STATE_DEFAULT -> {}
        }
    }

    private fun playTimeRender(time: Int) {
        when (viewModel?.stateLiveData?.value) {
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
}

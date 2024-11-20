package com.example.playlistmaker.ui.player

import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.ui.tracks.SearchActivity.Companion.TRACK
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.creator.PlayerCreator
import com.example.playlistmaker.data.mappers.TrackListMapper
import com.example.playlistmaker.domain.models.MediaPlayerState
import com.example.playlistmaker.domain.models.TrackData
import com.example.playlistmaker.utils.DateUtils
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val DELAY = 500L
        private const val REFRESH_PLAY_TIME = 29900L
    }

    private lateinit var track: TrackData
    private lateinit var trackTime: TextView
    private lateinit var playButton: ImageView
    private val playerRepository by lazy { Creator.getPlayerCreator(track) }

    private val preparePlayerUseCase by lazy { playerRepository.providePreparePlayerUseCase() }
    private val pauseTrackUseCase by lazy { playerRepository.providePauseTrackUseCase() }
    private val playTrackUseCase by lazy { playerRepository.providePlayTrackUseCase() }

    private val playbackTrackUseCase by lazy { playerRepository.providePlaybackTrackUseCase() }

    private val getPlayingTrackTimeUseCase by lazy { playerRepository.providePlayTrackTimeUseCase() }
    private val getPlayerStateUseCase by lazy { playerRepository.provideGetPlayerStateUseCase() }
    private val destroyPlayerUseCase by lazy { playerRepository.provideDestroyPlayerUseCase() }
    lateinit var playerState: MediaPlayerState

    private var mainThreadHandler: Handler? = null
    private var currentTimePlayingMillis = 0
    private lateinit var updateTimeRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.arrow_back_player).setNavigationOnClickListener {
            finish()
        }

        track = Gson().fromJson(intent.getStringExtra(TRACK), TrackData::class.java)

        val albumCover = findViewById<ImageView>(R.id.album_cover)
        Glide.with(albumCover)
            .load(track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(albumCover.resources.getDimensionPixelSize(R.dimen.art_work)))
            .into(albumCover)

        val trackName = findViewById<TextView>(R.id.player_song_name)
        trackName.text = track.trackName

        val artistName = findViewById<TextView>(R.id.player_band)
        artistName.text = track.artistName

        trackTime = findViewById(R.id.player_time)
        mainThreadHandler = Handler(Looper.getMainLooper())

        val albumName = findViewById<TextView>(R.id.player_album_data)
        if (track.collectionName.isEmpty()) {
            albumName.visibility = View.GONE
        } else albumName.text = track.collectionName

        val releaseYear = findViewById<TextView>(R.id.player_year_data)
        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val year = formatDate?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
        releaseYear.text = year

        val genre = findViewById<TextView>(R.id.player_genre_data)
        genre.text = track.primaryGenreName

        val country = findViewById<TextView>(R.id.player_country_data)
        country.text = track.country

        playButton = findViewById(R.id.player_control)
        playerState = preparePlayerUseCase.execute {
            playButton.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_play_white else R.drawable.ic_player_play)
        }
        updateTimeRunnable = createUpdaterRunnable()
        playButton.setOnClickListener {
            playbackControl()
            mainThreadHandler?.post(updateTimeRunnable)
        }
    }

    private fun startPlayer() {
        playerState = playTrackUseCase.execute {
            playButton.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_pause_white else R.drawable.ic_player_pause)
        }
    }

    private fun pausePlayer() {
        playerState =
            pauseTrackUseCase.execute { playButton.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_play_white else R.drawable.ic_player_play) }
    }

    private fun playbackControl() {
        playerState = playbackTrackUseCase.execute(
            actionPause = { pausePlayer() }, actionPlaying = { startPlayer() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyPlayerUseCase.execute()
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return isDarkModeOn
    }

    private fun createUpdaterRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                currentTimePlayingMillis = getPlayingTrackTimeUseCase.execute()
                playerState = getPlayerStateUseCase.execute()
                trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimePlayingMillis)
                when (playerState) {
                    MediaPlayerState.STATE_PLAYING -> mainThreadHandler?.postDelayed(this, DELAY)
                    MediaPlayerState.STATE_PAUSED -> mainThreadHandler?.removeCallbacks(this)
                    MediaPlayerState.STATE_PREPARED -> {
                        mainThreadHandler?.removeCallbacks(this)
                        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
                        playButton.setImageResource(if (isDarkModeOn()) R.drawable.ic_player_play_white else R.drawable.ic_player_play)
                    }

                    MediaPlayerState.STATE_DEFAULT -> {}
                }
            }
        }

    }
}
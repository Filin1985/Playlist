package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.SearchActivity.Companion.TRACK
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utils.DateUtils
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
        private const val REFRESH_PLAY_TIME = 29900L
    }

    private lateinit var track: TrackData
    private lateinit var trackTime: TextView
    private lateinit var playButton: ImageView
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    private var mainThreadHandler: Handler? = null

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

        trackTime = findViewById<TextView>(R.id.player_time)
        mainThreadHandler = Handler(Looper.getMainLooper())

        val albumName = findViewById<TextView>(R.id.player_album_data)
        if(track.collectionName.isEmpty()) {
            albumName.visibility = View.GONE
        } else  albumName.text = track.collectionName

        val releaseYear = findViewById<TextView>(R.id.player_year_data)
        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val year = formatDate?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
        releaseYear.text = year

        val genre = findViewById<TextView>(R.id.player_genre_data)
        genre.text = track.primaryGenreName

        val country = findViewById<TextView>(R.id.player_country_data)
        country.text = track.country

        playButton = findViewById(R.id.player_control)
        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_player_play)
            trackTime.text = getText(R.string.default_time)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_player_pause)
        playerState = STATE_PLAYING
        mainThreadHandler?.postDelayed(
            object: Runnable {
                override fun run() {
                    trackTime.text = if (mediaPlayer.currentPosition < REFRESH_PLAY_TIME) {
                        DateUtils.msToMMSSFormat(mediaPlayer.currentPosition)
                    } else {
                        getText(R.string.default_time)
                    }
                    mainThreadHandler?.postDelayed(
                        this,
                        DELAY,
                    )
                }
            },
            DELAY
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_player_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
}
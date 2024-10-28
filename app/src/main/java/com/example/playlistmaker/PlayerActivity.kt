package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.SearchActivity.Companion.TRACK
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utils.DateUtils
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.arrow_back_player).setNavigationOnClickListener {
            finish()
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK), TrackData::class.java)

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

        val trackTime = findViewById<TextView>(R.id.player_time)
        trackTime.text = DateUtils.msToMMSSFormat(track.trackTimeMillis)

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

    }
}
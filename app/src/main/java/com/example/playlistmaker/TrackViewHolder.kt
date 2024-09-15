package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utils.DateUtils

class TracksViewHolder(parentView: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parentView.context).inflate(R.layout.song_card, parentView, false)) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.artworkUrl)

    fun bind(model: TrackData) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTimeMillis.text = DateUtils.msToMMSSFormat(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.default_art_work)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.art_work)))
            .into(artworkUrl100)
    }
}
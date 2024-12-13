package com.example.playlistmaker.presentation

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySongCardBinding
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.utils.DateUtils

class TracksViewHolder(private val binding: ActivitySongCardBinding) :
    ViewHolder(binding.root) {
        fun bind(model: TrackData) {
            binding.trackName.text = model.trackName
            binding.artistName.text = model.artistName
            binding.trackTime.text = DateUtils.msToMMSSFormat(model.trackTimeMillis)
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.default_art_work)
                .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.art_work)))
                .into(binding.artworkUrl)
        }
    }
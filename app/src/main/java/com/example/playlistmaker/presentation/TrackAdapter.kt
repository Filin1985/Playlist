package com.example.playlistmaker.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.TrackData


class TracksAdapter(
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {

    var tracks = mutableListOf<TrackData>()

    fun interface ClickListener {
        fun click(track: TrackData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.click(tracks[position]) }
    }

    override fun getItemCount(): Int = tracks.size
}
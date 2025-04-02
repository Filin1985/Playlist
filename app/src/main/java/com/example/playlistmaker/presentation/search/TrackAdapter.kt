package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySongCardBinding
import com.example.playlistmaker.domain.search.model.TrackData


class TrackAdapter(
    private val tracks: MutableList<TrackData>
) : RecyclerView.Adapter<TracksViewHolder>() {

    var eventListener: (TrackData) -> Unit = {}
    var longClickListener: (TrackData) -> Unit = { }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivitySongCardBinding.inflate(inflater, parent, false)
        return TracksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { eventListener.invoke(tracks[position]) }

        holder.itemView.setOnLongClickListener {
            longClickListener.invoke(tracks[position])
            true
        }
    }

    override fun getItemCount(): Int = tracks.size
}

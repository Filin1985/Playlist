package com.example.playlistmaker.ui.player.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBottomSheetItemBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlayerBottomSheetAdapter(private val playlists: List<Playlist>) :
    RecyclerView.Adapter<PlayerBottomSheetViewHolder>() {
    var listener: (Playlist) -> Unit = { }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerBottomSheetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlayerBottomSheetViewHolder(
            PlaylistBottomSheetItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlayerBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            listener.invoke(playlists[position])
        }
    }
}
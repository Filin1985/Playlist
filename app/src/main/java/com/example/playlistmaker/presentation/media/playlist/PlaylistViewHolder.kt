package com.example.playlistmaker.presentation.media.playlist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlaylistViewHolder(private val viewBinding: PlaylistItemBinding) :
    ViewHolder(viewBinding.root) {
    fun bind(playlist: Playlist) {
        viewBinding.apply {
            Glide.with(itemView).load(playlist.coverUri)
                .apply(RequestOptions().placeholder(R.drawable.default_art_work_player)).transform(
                    CenterCrop(),
                    RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius))
                ).into(cover)
            title.text = playlist.title
            size.text = itemView.context.resources.getQuantityString(R.plurals.playlist_plurals, playlist.size, playlist.size)
        }
    }
}
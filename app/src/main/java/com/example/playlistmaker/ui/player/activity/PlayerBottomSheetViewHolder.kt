package com.example.playlistmaker.ui.player.activity

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBottomSheetItemBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist

class PlayerBottomSheetViewHolder(private val viewBinding: PlaylistBottomSheetItemBinding) :
    ViewHolder(viewBinding.root) {

    fun bind(playlist: Playlist) {
        viewBinding.apply {
            Glide.with(itemView)
                .load(playlist.coverUri)
                .signature(ObjectKey(playlist.id))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_art_work)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_item))
                )
                .into(cover)
            title.text = playlist.title
            size.text = itemView.resources.getQuantityString(
                R.plurals.playlist_plurals,
                playlist.size,
                playlist.size
            )
        }
    }
}
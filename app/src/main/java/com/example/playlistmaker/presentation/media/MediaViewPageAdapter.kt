package com.example.playlistmaker.presentation.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.favourite.fragment.MediaFragmentFavourite
import com.example.playlistmaker.ui.media.playlist.fragment.MediaPlaylistFragment

class MediaViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FRAGMENTS_NUMBER

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediaFragmentFavourite.newInstance()
            1 -> MediaPlaylistFragment.newInstance()
            else -> MediaPlaylistFragment.newInstance()
        }
    }

    companion object {
        private const val FRAGMENTS_NUMBER = 2
    }
}
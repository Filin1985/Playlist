package com.example.playlistmaker.presentation.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.fragment.MediaFragmentFavourite
import com.example.playlistmaker.ui.media.fragment.MediaFragmentPlaylist

class MediaViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FRAGMENTS_NUMBER

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediaFragmentFavourite.newInstance()
            1 -> MediaFragmentPlaylist.newInstance()
            else -> MediaFragmentPlaylist.newInstance()
        }
    }

    companion object {
        private const val FRAGMENTS_NUMBER = 2
    }
}
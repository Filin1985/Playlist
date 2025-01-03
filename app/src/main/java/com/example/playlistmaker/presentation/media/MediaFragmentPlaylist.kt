package com.example.playlistmaker.presentation.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaPlaylistBinding

class MediaFragmentPlaylist: Fragment() {
    companion object {
        private const val PAGE_NUMBER = "page_number"

        fun newInstance() = MediaFragmentPlaylist()
    }

    private lateinit var binding: FragmentMediaPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

}
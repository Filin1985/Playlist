package com.example.playlistmaker.presentation.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaFavouriteBinding

class MediaFragmentFavourite: Fragment() {
    companion object {
        private const val PAGE_NUMBER = "page_number"

        fun newInstance() = MediaFragmentFavourite()
    }

    private lateinit var binding: FragmentMediaFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }
}
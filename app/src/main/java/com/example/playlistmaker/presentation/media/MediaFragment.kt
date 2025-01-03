package com.example.playlistmaker.presentation.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment: Fragment() {
    companion object {
        private const val PAGE_NUMBER = "page_number"

        fun newInstance(number: Int) = MediaFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE_NUMBER, number)
            }
        }
    }

    private lateinit var binding: FragmentMediaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.number.text = requireArguments().getInt(PAGE_NUMBER).toString()
    }
}
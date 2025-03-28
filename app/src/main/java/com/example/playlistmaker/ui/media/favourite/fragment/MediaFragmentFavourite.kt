package com.example.playlistmaker.ui.media.favourite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaFavouriteBinding
import com.example.playlistmaker.domain.favorites.model.FavoriteTracksState
import com.example.playlistmaker.domain.search.model.TrackData
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModel
import com.example.playlistmaker.ui.player.fragment.PlayerFragment
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentFavourite : Fragment() {
    private var _binding: FragmentMediaFavouriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MediaFavouriteViewModel by viewModel()

    private var trackAdapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter(viewModel.favoritesTrackListLiveData.value!!)
        trackAdapter?.eventListener = clickOnTrackListener()
        binding.favoriteTracksHistoryRecyclerView.adapter = trackAdapter
        binding.favoriteTracksHistoryRecyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                true
            ).also { it.stackFromEnd = true }

        viewModel.favoriteTrackStateLiveData.observe(viewLifecycleOwner) {
            setState(it)
        }
    }

    private fun clickOnTrackListener(): (TrackData) -> Unit {
        return { track: TrackData ->
            if (viewModel.isClickAllowedLiveData.value!!) {
                viewModel.clickOnTrackDebounce(track)
                findNavController().navigate(
                    R.id.action_mediaFragment_to_playerActivity,
                    PlayerFragment.createArgs(Gson().toJson(track))
                )
            }
        }
    }

    private fun setState(favoriteState: FavoriteTracksState) {
        when(favoriteState) {
            FavoriteTracksState.FAVORITE_LIST -> {
                with(binding) {
                    notFoundImage.isVisible = false
                    emptyMedia.isVisible = false
                    favoriteTracksHistoryRecyclerView.isVisible = true
                }
                trackAdapter?.notifyDataSetChanged()
            }
            FavoriteTracksState.EMPTY_FAVORITE_DATA -> {
                with(binding) {
                    notFoundImage.isVisible = true
                    emptyMedia.isVisible = true
                    favoriteTracksHistoryRecyclerView.isVisible = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        trackAdapter = null
    }

    companion object {
        fun newInstance() = MediaFragmentFavourite()
    }
}
package com.example.playlistmaker.ui.media.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaPlaylistBinding
import com.example.playlistmaker.domain.mediateca.playlists.model.Playlist
import com.example.playlistmaker.domain.playlist.model.PlaylistStatus
import com.example.playlistmaker.presentation.media.playlist.PlaylistAdapter
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistFragment : Fragment() {
    private var _binding: FragmentMediaPlaylistBinding? = null
    private val binding get() = _binding!!

    private var _adapter: PlaylistAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: MediaPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playListMediaFragment_to_newPlaylist
            )
        }

        viewModel.showPlaylists()

        _adapter = PlaylistAdapter(viewModel.playlistLiveData.value!!)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.playlistStatusLiveData.observe(viewLifecycleOwner) {
            renderResult(it)
        }
    }

    private fun renderResult(resultStatus: PlaylistStatus<List<Playlist>>) {
        when (resultStatus) {
            is PlaylistStatus.Content -> {
                with(binding) {
                    notFoundImage.isVisible = false
                    notFound.isVisible = false
                    recyclerView.isVisible = true
                }
            }

            is PlaylistStatus.Empty, null -> {
                with(binding) {
                    notFoundImage.isVisible = true
                    notFound.isVisible = true
                    recyclerView.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MediaPlaylistFragment()
    }
}
package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.compose.MediaScreen
import com.example.playlistmaker.ui.media.favourite.view_model.MediaFavouriteViewModelStateFlow
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModel
import com.example.playlistmaker.ui.media.playlist.view_model.MediaPlaylistViewModelStateFlow
import com.example.playlistmaker.ui.player.fragment.PlayerFragment
import com.example.playlistmaker.ui.playlistDetails.fragment.DetailPlaylistFragment
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentWithComposeUI : Fragment() {
    private val favouriteViewModel: MediaFavouriteViewModelStateFlow by viewModel()
    private val playlistViewModel: MediaPlaylistViewModelStateFlow by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MediaScreen(
                    favouriteViewModel = favouriteViewModel,
                    onTrackClick = { track ->
                        findNavController().navigate(
                            R.id.action_mediaFragment_to_playerActivity,
                            PlayerFragment.createArgs(Gson().toJson(track))
                        )
                    },
                    onPlaylistClick = { playlist ->
                        findNavController().navigate(
                            R.id.action_playListMediaFragment_to_playlistDetail,
                            DetailPlaylistFragment.createArgs(playlist.id)
                        )
                    },
                    onAddNewPlaylistClick = {
                        findNavController().navigate(
                            R.id.action_playListMediaFragment_to_newPlaylist,
                        )
                    },
                    playlistViewModel = playlistViewModel
                )
            }
        }
    }
}
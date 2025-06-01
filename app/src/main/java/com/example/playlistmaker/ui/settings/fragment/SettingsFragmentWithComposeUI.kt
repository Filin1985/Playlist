package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.ui.media.compose.MediaScreen
import com.example.playlistmaker.ui.player.fragment.PlayerFragment
import com.example.playlistmaker.ui.playlistDetails.fragment.DetailPlaylistFragment
import com.example.playlistmaker.ui.settings.compose.SettingsScreen
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModelWithStateFLow
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragmentWithComposeUI : Fragment() {
    private val themeViewModel: ThemeViewModelWithStateFLow by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreen(themeViewModel, settingsViewModel)
            }
        }
    }
}
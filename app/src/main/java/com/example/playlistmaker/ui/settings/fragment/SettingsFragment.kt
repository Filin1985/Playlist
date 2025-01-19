package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val themeViewModel: ThemeViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeViewModel.liveData.observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = when(it) {
                is Theme.LightTheme -> false
                is Theme.DarkTheme -> true
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            val theme = if (isChecked) Theme.DarkTheme() else Theme.LightTheme()
            themeViewModel.switchAndSaveTheme(theme)

        }

        themeViewModel.getPrevTheme()

        binding.share.setOnClickListener {
            settingsViewModel.shareApp(getString(R.string.share_link))
        }

        binding.support.setOnClickListener {
            settingsViewModel.mailToSupport(
                EmailData(
                    email = getString(R.string.support_email),
                    subject = getString(R.string.support_subject),
                    link = getString(R.string.support_text)
                )
            )
        }

        binding.agreement.setOnClickListener {
            settingsViewModel.openTerms(getString(R.string.agreement_link))
        }
    }
}
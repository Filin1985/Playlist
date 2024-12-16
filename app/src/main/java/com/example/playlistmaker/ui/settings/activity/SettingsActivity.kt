package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val themeViewModel: ThemeViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBack.setOnClickListener {
            finish()
        }

        themeViewModel.liveData.observe(this) {
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
package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.model.Theme
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.ThemeViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModel.factory()
    }
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModel.factory()
    }

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
            settingsViewModel.shareApp()
        }

        val support = findViewById<ImageView>(R.id.support)
        support.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
                startActivity(Intent.createChooser(this, null))
            }
        }

        val agreement = findViewById<ImageView>(R.id.agreement)
        agreement.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(agreementIntent)
        }
    }
}
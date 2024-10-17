package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBack = findViewById<ImageView>(R.id.arrow_back)

        arrowBack.setOnClickListener {
            finish()
        }

        val app = (applicationContext as App)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = getSharedPreferences(App.SETTINGS, MODE_PRIVATE)
            .getBoolean(DARK_THEME, app.darkTheme)
        themeSwitcher.setOnCheckedChangeListener {switcher, checked ->
            app.switchTheme(checked)
            app.saveTheme(checked)
        }

        val shareButton = findViewById<ImageView>(R.id.share)
        shareButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
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
package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_TEXT, link)
        }
        val chooser: Intent = Intent.createChooser(shareIntent, "").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
    }

    override fun openLink(link: String) {
        val agreementIntent =
            Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = Uri.parse(link)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
        context.startActivity(agreementIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.link)
        }
        context.startActivity(supportIntent)
    }

    override fun sendMessage(message: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser: Intent = Intent.createChooser(shareIntent, "").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
    }
}
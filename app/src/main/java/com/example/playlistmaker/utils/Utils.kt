package com.example.playlistmaker.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.google.android.material.snackbar.Snackbar

object Utils {
    fun showSnackbar(
        view: View,
        message: String,
        context: Context
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val backgroundColor = ContextCompat.getColor(context, R.color.black_day_white_night)
        val snackTextColor = ContextCompat.getColor(context, R.color.yp_white)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textSize = 16f
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.setTextColor(snackTextColor)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }
}
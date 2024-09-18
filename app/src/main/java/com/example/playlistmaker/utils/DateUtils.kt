package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun msToMMSSFormat(timeInMS: Int) = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMS)
}
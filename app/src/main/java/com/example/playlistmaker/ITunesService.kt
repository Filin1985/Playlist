package com.example.playlistmaker

import com.example.playlistmaker.api.ITunesAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesService {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(
        GsonConverterFactory.create()
    ).build()

    private val itunesService = retrofit.create(ITunesAPI::class.java)

    fun get(): ITunesAPI = itunesService
}
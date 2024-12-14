package com.example.playlistmaker.data.search.impl

import android.content.Context
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.model.TrackData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {
    companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    private val sharedPreferences = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)

    override fun getTrackList(): ArrayList<TrackData> {
        val inputJSON = sharedPreferences.getString(SEARCH_HISTORY, "")
        return if (inputJSON.isNullOrEmpty()) {
            arrayListOf()
        } else {
            Gson().fromJson(inputJSON, object : TypeToken<Collection<TrackData>>() {}.type)
        }
    }

    override fun addTrackToList(track: TrackData) {
        val trackList = getTrackList()
        trackList.remove(track)
        trackList.add(0, track)
        if (trackList.size > 10) {
            trackList.removeLast()
        }
        saveTrackList(trackList)
    }

    override fun clearTrackList() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

    private fun saveTrackList(trackList: ArrayList<TrackData>) {
        val trackToSaveJSON = Gson().toJson(trackList)
        sharedPreferences.edit().putString(SEARCH_HISTORY, trackToSaveJSON).apply()
    }
}


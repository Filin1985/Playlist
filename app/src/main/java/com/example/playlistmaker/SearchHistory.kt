package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPreferences: SharedPreferences) {
    companion object{
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    fun getTrackList(): ArrayList<TrackData> {
        val inputJSON = sharedPreferences.getString(SEARCH_HISTORY, "")
        return if(inputJSON.isNullOrEmpty()) {
            arrayListOf()
        } else {
            Gson().fromJson(inputJSON, object: TypeToken<Collection<TrackData>>(){}.type)
        }
    }

    fun clearTrackList() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

    private fun saveTrackList(trackList: ArrayList<TrackData>) {
        val trackToSaveJSON = Gson().toJson(trackList)
        sharedPreferences.edit().putString(SEARCH_HISTORY, trackToSaveJSON).apply()
    }

    fun addTrackToList(track: TrackData) {
        val trackList = getTrackList()
        trackList.remove(track)
        trackList.add(0, track)
        if(trackList.size > 10) {
            trackList.removeLast()
        }
        saveTrackList(trackList)
    }
}


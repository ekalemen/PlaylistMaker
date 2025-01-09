package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val SEARCH_TRACK_HISTORY = "search_track_history"
const val HISTORY_SIZE = 10

class SearchHistory(private val pf: SharedPreferences) {

    private var trackList = mutableListOf<Track>()
    private val gson = Gson()

    fun getSavedHistory(): MutableList<Track> {
        val str = pf.getString(SEARCH_TRACK_HISTORY, null)
        if (str != null) {
            trackList = mutableListOf<Track>().apply {
                addAll( gson.fromJson(str,Array<Track>::class.java ))}
        }
        return trackList
    }

    fun addTrackToHistory(track: Track) {
        if (getIndexOfTrack(track.trackId) != null)
            trackList.remove(track)
        else if (trackList.size >= HISTORY_SIZE)
            trackList.removeAt(trackList.lastIndex)
        trackList.add(0, track)
        saveTrackHistory()
    }

    fun clearTrackHistory() {
        trackList.clear()
        pf.edit()
            .remove(SEARCH_TRACK_HISTORY)
            .apply()
    }

    fun saveTrackHistory() {
        val str = gson.toJson(trackList)
        pf.edit()
            .putString(SEARCH_TRACK_HISTORY, str)
            .apply()
    }

    fun getHistoryTracks(): MutableList<Track>{
        return trackList
    }

    private fun getIndexOfTrack(trackId: Int): Int? {
        for ((index, track) in trackList.withIndex()) {
            if (track.trackId == trackId) return index
        }
        return null
    }
}
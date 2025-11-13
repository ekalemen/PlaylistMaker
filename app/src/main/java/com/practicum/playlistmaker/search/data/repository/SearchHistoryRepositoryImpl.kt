package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val pf: SharedPreferences) : SearchHistoryRepository {

    private val searchTrackHistory = "search_track_history"

    override fun loadTrackHistory(): String {
        return pf.getString(searchTrackHistory, null) ?: ""
    }
    override fun clearTrackHistory() {
        pf.edit()
            .remove(searchTrackHistory)
            .apply()
    }
    override fun saveTrackHistory(str: String) {
        pf.edit()
            .putString(searchTrackHistory, str)
            .apply()
    }
}
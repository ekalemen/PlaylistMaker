package com.practicum.playlistmaker.search.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    private val searchTrackHistory = "search_track_history"

    private val pf: SharedPreferences = context.getSharedPreferences(
        Creator.PLAYLIST_MAKER_PREFS,
        Context.MODE_PRIVATE
    )
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
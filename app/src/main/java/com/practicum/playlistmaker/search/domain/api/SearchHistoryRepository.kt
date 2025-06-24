package com.practicum.playlistmaker.search.domain.api

interface SearchHistoryRepository {
    fun loadTrackHistory(): String
    fun clearTrackHistory()
    fun saveTrackHistory(str: String)
}
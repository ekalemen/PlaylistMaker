package com.practicum.playlistmaker.domain.api

interface SearchHistoryRepository {
    fun loadTrackHistory(): String
    fun clearTrackHistory()
    fun saveTrackHistory(str: String)
}
package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getSavedHistory() : MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun clearTrackHistory()
    fun saveTrackHistory()
    fun getHistoryTracks(): MutableList<Track>
}

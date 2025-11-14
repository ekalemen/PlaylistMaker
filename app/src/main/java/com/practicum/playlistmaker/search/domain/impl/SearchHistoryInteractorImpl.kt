package com.practicum.playlistmaker.search.domain.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

const val HISTORY_SIZE = 10

class SearchHistoryInteractorImpl(
    private val shRepository: SearchHistoryRepository,
    private val gson : Gson
) :  SearchHistoryInteractor {

    private var trackList = mutableListOf<Track>()

    override fun getSavedHistory(): MutableList<Track> {
        val str = shRepository.loadTrackHistory()
        if (str.isNotEmpty()) {
            trackList = mutableListOf<Track>().apply {
                addAll( gson.fromJson(str,Array<Track>::class.java ))}
        }
        return trackList
    }

    override fun addTrackToHistory(track: Track) {
        if (getIndexOfTrack(track.trackId) != null)
            trackList.remove(track)
        else if (trackList.size >= HISTORY_SIZE)
            trackList.removeAt(trackList.lastIndex)
        trackList.add(0, track)
        saveTrackHistory()
    }

    override fun clearTrackHistory() {
        trackList.clear()
        shRepository.clearTrackHistory()
    }

    override fun saveTrackHistory() {
        val str = gson.toJson(trackList)
        shRepository.saveTrackHistory(str)
    }

    override fun getHistoryTracks(): MutableList<Track>{
        return trackList
    }

    private fun getIndexOfTrack(trackId: Int): Int? {
        for ((index, track) in trackList.withIndex()) {
            if (track.trackId == trackId) return index
        }
        return null
    }
}
package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchTracksResult

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(searchRes: SearchTracksResult)
    }
}
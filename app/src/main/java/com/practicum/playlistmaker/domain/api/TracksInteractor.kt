package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchTracksResult

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(searchRes: SearchTracksResult)
    }
}
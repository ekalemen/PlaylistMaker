package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchTracksResult

interface TracksRepository {
    fun search(track: String): SearchTracksResult
}
package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.SearchTracksResult

interface TracksRepository {
    fun search(track: String): SearchTracksResult
}
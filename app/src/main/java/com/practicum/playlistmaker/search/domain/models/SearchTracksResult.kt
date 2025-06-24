package com.practicum.playlistmaker.search.domain.models

data class SearchTracksResult(
    val resultCode: Int,
    val tracks: List<Track>
)

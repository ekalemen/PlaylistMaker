package com.practicum.playlistmaker.domain.models

data class SearchTracksResult(
    val resultCode: Int,
    val tracks: List<Track>
)

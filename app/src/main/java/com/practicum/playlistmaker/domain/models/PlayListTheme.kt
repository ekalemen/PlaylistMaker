package com.practicum.playlistmaker.domain.models

enum class ThemeType {
    LIGHT,
    DARK
}
data class PlayListTheme(val theme: ThemeType)

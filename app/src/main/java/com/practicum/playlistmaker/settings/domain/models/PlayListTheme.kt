package com.practicum.playlistmaker.settings.domain.models

enum class ThemeType {
    LIGHT,
    DARK
}
data class PlayListTheme(val theme: ThemeType)

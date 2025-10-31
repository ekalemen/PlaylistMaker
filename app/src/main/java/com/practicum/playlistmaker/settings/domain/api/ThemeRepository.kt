package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.PlayListTheme

interface ThemeRepository {
    fun getSavedTheme(): PlayListTheme
    fun saveTheme(theme: PlayListTheme)
    fun hasSavedTheme(): Boolean
    fun switchTheme(theme: PlayListTheme)
}
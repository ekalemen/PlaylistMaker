package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.PlayListTheme

interface ThemeRepository {
    fun getSavedTheme(): PlayListTheme
    fun saveTheme(theme: PlayListTheme)
    fun hasSavedTheme(): Boolean
}
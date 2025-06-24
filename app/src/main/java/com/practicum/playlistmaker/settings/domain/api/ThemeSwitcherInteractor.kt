package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.PlayListTheme

interface ThemeSwitcherInteractor {
    fun switchTheme(theme: PlayListTheme)
    fun switchThemeSaved()
    fun getSavedTheme(): PlayListTheme?
}
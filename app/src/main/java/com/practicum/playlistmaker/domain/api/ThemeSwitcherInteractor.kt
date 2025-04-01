package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.PlayListTheme

interface ThemeSwitcherInteractor {
    fun switchTheme(theme: PlayListTheme)
    fun switchThemeSaved()
    fun getSavedTheme(): PlayListTheme?
}
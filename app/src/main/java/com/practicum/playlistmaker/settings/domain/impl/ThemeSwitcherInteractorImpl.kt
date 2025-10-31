package com.practicum.playlistmaker.settings.domain.impl


import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme

class ThemeSwitcherInteractorImpl(private val tRep: ThemeRepository): ThemeSwitcherInteractor {
    override fun switchTheme(theme: PlayListTheme) {
        tRep.switchTheme(theme)
        tRep.saveTheme(theme)
    }

    override fun switchThemeSaved() {
        if (tRep.hasSavedTheme()) {
            switchTheme(tRep.getSavedTheme())
        }
    }

    override fun getSavedTheme(): PlayListTheme? {
        if (tRep.hasSavedTheme()) {
            return tRep.getSavedTheme()
        }
        return null
    }
}
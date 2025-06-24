package com.practicum.playlistmaker.settings.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType

class ThemeSwitcherInteractorImpl(private val tRep: ThemeRepository): ThemeSwitcherInteractor {
    override fun switchTheme(theme: PlayListTheme) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme.theme == ThemeType.DARK) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
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
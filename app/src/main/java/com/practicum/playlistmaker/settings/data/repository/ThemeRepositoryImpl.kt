package com.practicum.playlistmaker.settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType

class ThemeRepositoryImpl(private val pf: SharedPreferences): ThemeRepository {
    private val themePreference = "theme_switch"

    override fun getSavedTheme(): PlayListTheme {
        val theme = pf.getInt(themePreference, ThemeType.LIGHT.ordinal)
        return PlayListTheme(ThemeType.entries[theme])
    }

    override fun saveTheme(theme: PlayListTheme) {
        pf.edit()
            .putInt(themePreference, theme.theme.ordinal)
            .apply()
    }

    override fun hasSavedTheme(): Boolean {
        return pf.contains(themePreference)
    }

    override fun switchTheme(theme: PlayListTheme) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme.theme == ThemeType.DARK) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
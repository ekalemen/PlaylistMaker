package com.practicum.playlistmaker.settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType

class ThemeRepositoryImpl(context: Context): ThemeRepository {
    private val themePreference = "theme_switch"
    private val pf: SharedPreferences = context.getSharedPreferences(
        Creator.PLAYLIST_MAKER_PREFS,
        Context.MODE_PRIVATE
    )

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
package com.practicum.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.models.PlayListTheme
import com.practicum.playlistmaker.domain.models.ThemeType

class ThemeRepositoryImpl(context: Context): ThemeRepository{
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
}
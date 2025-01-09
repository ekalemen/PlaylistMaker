package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFS = "playlist_maker_prefs"
const val THEME_SWITCH = "theme_switch"

class App : Application() {
    var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        val savedThemeSwitch = sharedPreferences.getBoolean(THEME_SWITCH, false)
        switchTheme(savedThemeSwitch)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit()
            .putBoolean(THEME_SWITCH, darkThemeEnabled)
            .apply()
    }
} 
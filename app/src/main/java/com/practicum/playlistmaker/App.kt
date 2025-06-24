package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor

class App : Application() {
    private lateinit var themeSwitcher: ThemeSwitcherInteractor
    override fun onCreate() {
        super.onCreate()
        themeSwitcher = Creator.provideThemeSwitcherInteractor(this)
        themeSwitcher.switchThemeSaved()
    }
} 
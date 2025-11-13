package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import com.practicum.playlistmaker.player.di.PlayerModule
import com.practicum.playlistmaker.search.di.SearchModule
import com.practicum.playlistmaker.settings.di.SettingsModule
import org.koin.android.ext.android.inject

class App : Application() {
    private val themeSwitcher: ThemeSwitcherInteractor by inject()
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(PlayerModule, SettingsModule, SearchModule)
        }

        themeSwitcher.switchThemeSaved()
    }
} 
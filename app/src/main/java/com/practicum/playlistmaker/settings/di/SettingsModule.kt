package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.impl.ThemeSwitcherInteractorImpl
import com.practicum.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SettingsModule = module {
    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
    factory<ThemeSwitcherInteractor> {
        ThemeSwitcherInteractorImpl(get())
    }
    factory<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }
    viewModel {
        SettingsViewModel(get(),get())
    }
}
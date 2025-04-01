package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.ThemeSwitcherInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    const val PLAYLIST_MAKER_PREFS = "playlist_maker_prefs"

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun provideThemeSwitcherInteractor(context: Context): ThemeSwitcherInteractor {
        return ThemeSwitcherInteractorImpl(getThemeRepository(context))
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
}
package com.practicum.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SearchModule = module {
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory {
        androidContext().getSharedPreferences("playlist_maker_prefs", Context.MODE_PRIVATE)
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(RetrofitNetworkClient())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get(),get())
    }
}
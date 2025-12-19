package com.practicum.playlistmaker.media.di

import com.practicum.playlistmaker.media.ui.viewmodel.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MediaKoinModule = module {
    viewModel { FavoritesViewModel() }
    viewModel { PlaylistsViewModel() }
}
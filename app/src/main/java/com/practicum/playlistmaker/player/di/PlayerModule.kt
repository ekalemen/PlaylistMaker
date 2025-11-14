package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.viewmodel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlayerModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory {
        MediaPlayer()
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    viewModel {
        PlayerViewModel(get())
    }
}
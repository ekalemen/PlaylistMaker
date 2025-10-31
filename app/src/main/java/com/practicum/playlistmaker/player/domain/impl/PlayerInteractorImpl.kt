package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(url: String?, onPrepareListenerCallback: () -> (Unit), onCompleteListenerCallback: () -> (Unit)) {
        repository.preparePlayer(url, onPrepareListenerCallback, onCompleteListenerCallback)
    }

    override fun startPlayer() {
     repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun destroyPlayer() {
        repository.destroyPlayer()
    }

    override fun getPlayerPosition(): Int {
        return repository.getPlayerPosition()
    }
}
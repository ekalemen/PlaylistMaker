package com.practicum.playlistmaker.player.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String?, onPrepareListenerCallback: () -> (Unit), onCompleteListenerCallback: () -> (Unit))
    fun startPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun getPlayerPosition(): Int
}
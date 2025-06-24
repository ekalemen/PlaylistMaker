package com.practicum.playlistmaker.player.domain.api

enum class PlayerStatus {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}

interface PlayerInteractor {
    fun preparePlayer(url: String?, onPrepareListenerCallback: () -> (Unit), onCompleteListenerCallback: () -> (Unit))
    fun startPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun getPlayerStatus(): PlayerStatus
    fun getPlayerPosition(): Int
}
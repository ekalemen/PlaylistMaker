package com.practicum.playlistmaker.player.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerStatus

class PlayerInteractorImpl: PlayerInteractor {
    private var playerState: PlayerStatus = PlayerStatus.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String?, onPrepareListenerCallback: () -> (Unit), onCompleteListenerCallback: () -> (Unit)) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStatus.STATE_PREPARED
            onPrepareListenerCallback()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStatus.STATE_PREPARED
            onCompleteListenerCallback()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStatus.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStatus.STATE_PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
        playerState = PlayerStatus.STATE_DEFAULT
    }

    override fun getPlayerStatus(): PlayerStatus {
        return playerState
    }

    override fun getPlayerPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerStatus

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {
    override fun preparePlayer(url: String?, onPrepareListenerCallback: () -> (Unit), onCompleteListenerCallback: () -> (Unit))
    {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepareListenerCallback()
        }
        mediaPlayer.setOnCompletionListener {
            onCompleteListenerCallback()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }
    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getPlayerPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
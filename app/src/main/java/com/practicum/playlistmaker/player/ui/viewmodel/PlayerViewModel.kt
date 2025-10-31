package com.practicum.playlistmaker.player.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel : ViewModel() {
    private var playerInteractor=  Creator.providePlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private val _playerStatus = MutableLiveData<PlayerStatus>()
    val playerStatus: LiveData<PlayerStatus> = _playerStatus

    private val _playerPlayingTime = MutableLiveData<String>()
    val playerPlayingTime: LiveData<String> = _playerPlayingTime

    fun preparePlayer(track: Track) {
        _playerStatus.postValue(PlayerStatus.STATE_DEFAULT)
        playerInteractor.preparePlayer(
            track.previewUrl,
            {
                _playerStatus.postValue(PlayerStatus.STATE_PREPARED)
                _playerPlayingTime.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(0))
            },
            {
                _playerStatus.postValue(PlayerStatus.STATE_COMPLETED)
                _playerPlayingTime.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(0))
            }
        )
    }

    fun playbackControl() {
        when (_playerStatus.value) {
            PlayerStatus.STATE_PLAYING -> pausePlayer()
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            else -> pausePlayer()
        }
    }
    fun pausePlayer() {
        handler.removeCallbacks(timeUpdatingInterval)
        playerInteractor.pausePlayer()
        _playerStatus.postValue(PlayerStatus.STATE_PAUSED)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        handler.post(timeUpdatingInterval)
        _playerStatus.postValue(PlayerStatus.STATE_PLAYING)
    }

    fun destroyPlayer() {
        handler.removeCallbacks(timeUpdatingInterval)
        playerInteractor.destroyPlayer()
    }

    private val timeUpdatingInterval = object : Runnable {
        override fun run() {
                _playerPlayingTime.postValue(SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getPlayerPosition()))
                handler.postDelayed(this, UPDATE_TIME)
        }
    }

    companion object {
        private const val UPDATE_TIME = 330L
    }

}
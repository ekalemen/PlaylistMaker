package com.practicum.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerStatus
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.EXTRA_TRACK_INFO
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var previewUrl: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playerInteractor: PlayerInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val trackStr = intent.getStringExtra(EXTRA_TRACK_INFO)
        val gson = Gson()
        val track = gson.fromJson(trackStr, Track::class.java)

        playerInteractor = Creator.providePlayerInteractor()

        binding.playerButtonBack.setOnClickListener {
            finish()
        }

        Glide.with(binding.imageAlbumCover)
            .load(track.getCoverArtwork())
            .centerInside()
            .transform(RoundedCorners( TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8F, binding.imageAlbumCover.resources.displayMetrics
            ).toInt()))
            .placeholder(R.drawable.ic_track_placeholder)
            .into(binding.imageAlbumCover)

        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.durationTrack.text = track.trackDuration
        binding.albumTrack.text = track.collectionName
        binding.yearTrack.text = track.releaseDate.substring(0,4)
        binding.genreTRack.text = track.primaryGenreName
        binding.countryTrack.text = track.country

        previewUrl = track.previewUrl

        binding.playerButtonPlay.isEnabled = false

        playerInteractor.preparePlayer(previewUrl,
            { binding.playerButtonPlay.isEnabled = true },
            { binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
              handler.removeCallbacks(timeUpdatingInterval)
              binding.trackPlayingTime.setText(R.string.track_start_time) })

        binding.playerButtonPlay.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdatingInterval)
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeUpdatingInterval)
        playerInteractor.destroyPlayer()
    }

    private val timeUpdatingInterval = object : Runnable {
        override fun run() {
            if (playerInteractor.getPlayerStatus() == PlayerStatus.STATE_PLAYING) {
                binding.trackPlayingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getPlayerPosition())
                handler.postDelayed(this, UPDATE_TIME)
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        binding.playerButtonPlay.setImageResource(R.drawable.ic_pause_button)
        handler.post(timeUpdatingInterval)
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
        handler.removeCallbacks(timeUpdatingInterval)
    }

    private fun playbackControl() {
        when (playerInteractor.getPlayerStatus()) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {pausePlayer()}
        }
    }

    companion object {
        private const val UPDATE_TIME = 330L
    }
}
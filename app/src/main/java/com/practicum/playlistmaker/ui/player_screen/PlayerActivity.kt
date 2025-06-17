package com.practicum.playlistmaker.ui.player_screen

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerStatus
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search_screen.EXTRA_TRACK_INFO
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var buttonPlay: ImageView
    private lateinit var trackPlayingTime: TextView
    private var previewUrl: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playerInteractor: PlayerInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent
        val trackStr = intent.getStringExtra(EXTRA_TRACK_INFO)
        val gson = Gson()
        val track = gson.fromJson(trackStr, Track::class.java)

        playerInteractor = Creator.providePlayerInteractor()

        val playerButtonBack = findViewById<ImageView>(R.id.player_button_back)
        playerButtonBack.setOnClickListener {
            finish()
        }

        val albumCoverView = findViewById<ImageView>(R.id.imageAlbumCover)
        Glide.with(albumCoverView)
            .load(track.getCoverArtwork())
            .centerInside()
            .transform(RoundedCorners( TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8F, albumCoverView.resources.displayMetrics
            ).toInt()))
            .placeholder(R.drawable.ic_track_placeholder)
            .into(albumCoverView)

        val trackNameView = findViewById<TextView>(R.id.trackName)
        trackNameView.text = track.trackName

        val trackArtist = findViewById<TextView>(R.id.trackArtist)
        trackArtist.text = track.artistName

        val durationTrackTime = findViewById<TextView>(R.id.durationTrack)
        durationTrackTime.text = track.trackDuration

        val trackAlbum = findViewById<TextView>(R.id.albumTrack)
        trackAlbum.text = track.collectionName

        val trackYear = findViewById<TextView>(R.id.yearTrack)
        trackYear.text = track.releaseDate.substring(0,4)

        val trackGenre = findViewById<TextView>(R.id.genreTRack)
        trackGenre.text = track.primaryGenreName

        val trackCountry = findViewById<TextView>(R.id.countryTrack)
        trackCountry.text = track.country

        previewUrl = track.previewUrl

        buttonPlay = findViewById(R.id.player_button_play)
        buttonPlay.isEnabled = false

        playerInteractor.preparePlayer(previewUrl,
            { buttonPlay.isEnabled = true },
            { buttonPlay.setImageResource(R.drawable.ic_play_button)
              handler.removeCallbacks(timeUpdatingInterval)
              trackPlayingTime.setText(R.string.track_start_time) })

        buttonPlay.setOnClickListener {
            playbackControl()
        }

        trackPlayingTime = findViewById(R.id.trackPlayingTime)
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
                trackPlayingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playerInteractor.getPlayerPosition())
                handler.postDelayed(this, UPDATE_TIME)
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        buttonPlay.setImageResource(R.drawable.ic_pause_button)
        handler.post(timeUpdatingInterval)
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        buttonPlay.setImageResource(R.drawable.ic_play_button)
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
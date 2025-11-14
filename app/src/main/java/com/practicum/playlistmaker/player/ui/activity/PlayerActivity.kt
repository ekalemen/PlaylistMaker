package com.practicum.playlistmaker.player.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.api.PlayerStatus
import com.practicum.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.EXTRA_TRACK_INFO
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.playerStatus.observe(this) { status -> renderStatus(status) }
        viewModel.playerPlayingTime.observe(this) { time -> renderTrackTime(time) }

        val intent = intent
        @Suppress("DEPRECATION")
        val track = intent.getParcelableExtra<Track>(EXTRA_TRACK_INFO)

        binding.playerButtonBack.setOnClickListener {
            finish()
        }

        if (track != null) {
            Glide.with(binding.imageAlbumCover)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .centerInside()
                .transform(
                    RoundedCorners(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8F,
                            binding.imageAlbumCover.resources.displayMetrics
                        ).toInt()
                    )
                )
                .placeholder(R.drawable.ic_track_placeholder)
                .into(binding.imageAlbumCover)

            binding.trackName.text = track.trackName
            binding.trackArtist.text = track.artistName
            binding.durationTrack.text = track.trackDuration
            binding.albumTrack.text = track.collectionName
            binding.yearTrack.text = track.releaseDate.substring(0, 4)
            binding.genreTRack.text = track.primaryGenreName
            binding.countryTrack.text = track.country

            previewUrl = track.previewUrl

            viewModel.preparePlayer(track)
        }
        binding.playerButtonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    private fun renderStatus(state: PlayerStatus) {
        when (state) {
            PlayerStatus.STATE_DEFAULT -> {
                binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
                binding.playerButtonPlay.isEnabled = false
            }
            PlayerStatus.STATE_PREPARED -> {
                binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
                binding.playerButtonPlay.isEnabled = true
            }
            PlayerStatus.STATE_PLAYING -> {
                binding.playerButtonPlay.setImageResource(R.drawable.ic_pause_button)
                binding.playerButtonPlay.isEnabled = true
            }
            PlayerStatus.STATE_PAUSED -> {
                binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
                binding.playerButtonPlay.isEnabled = true
            }
            PlayerStatus.STATE_COMPLETED -> {
                binding.playerButtonPlay.setImageResource(R.drawable.ic_play_button)
                binding.playerButtonPlay.isEnabled = true
            }
        }
    }

    private fun renderTrackTime(time: String) {
        binding.trackPlayingTime.text = time
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
         viewModel.destroyPlayer()
    }
}
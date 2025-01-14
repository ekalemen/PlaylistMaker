package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent
        val trackStr = intent.getStringExtra(EXTRA_TRACK_INFO)
        val gson = Gson()
        val track = gson.fromJson(trackStr, Track::class.java)

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
        durationTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val trackAlbum = findViewById<TextView>(R.id.albumTrack)
        trackAlbum.text = track.collectionName

        val trackYear = findViewById<TextView>(R.id.yearTrack)
        trackYear.text = track.releaseDate.substring(0,4)

        val trackGenre = findViewById<TextView>(R.id.genreTRack)
        trackGenre.text = track.primaryGenreName

        val trackCountry = findViewById<TextView>(R.id.countryTrack)
        trackCountry.text = track.country
    }
}
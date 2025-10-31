package com.practicum.playlistmaker.search.ui.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val artist: TextView = itemView.findViewById(R.id.foundArtistName)
    private val trackName: TextView = itemView.findViewById(R.id.foundTrackName)
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
    private val trackTime: TextView = itemView.findViewById(R.id.foundTrackTime)

    fun bind(model: Track) {
        artist.setText(model.artistName)
        trackName.setText(model.trackName)
        trackTime.text = model.trackDuration
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder)
            .into(albumCover)
    }
}
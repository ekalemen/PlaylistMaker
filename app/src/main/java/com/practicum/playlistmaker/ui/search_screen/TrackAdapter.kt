package com.practicum.playlistmaker.ui.search_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track

class TrackAdapter(
    private val onItemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {
    private var tracks = mutableListOf<Track>()

    fun updateTracksList(newTracks: MutableList<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
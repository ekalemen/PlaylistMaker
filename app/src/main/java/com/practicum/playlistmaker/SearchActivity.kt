package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchActivity : AppCompatActivity() {
    var inputText: String = AMOUNT_DEF

    companion object {
        const val TEXT_AMOUNT = "TEXT_AMOUNT"
        const val AMOUNT_DEF = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_AMOUNT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(TEXT_AMOUNT, AMOUNT_DEF)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        val trackAdapter = TrackAdapter(MockTrackList.tracks)
        recyclerView.adapter = trackAdapter

        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        searchEditText.setText(inputText)

        val buttonSearchBack = findViewById<ImageView>(R.id.srch_button_back)

        buttonSearchBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        @SuppressLint("ClickableViewAccessibility")
        fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
            this.setOnTouchListener { v, event ->
                var hasConsumed = false
                if (v is EditText) {
                    if (event.x >= v.width - v.totalPaddingRight) {
                        if (event.action == MotionEvent.ACTION_UP) {
                            onClicked(this)
                        }
                        hasConsumed = true
                    }
                }
                hasConsumed
            }
        }

        searchEditText.onRightDrawableClicked {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            it.text.clear()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
                } else {
                    searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, R.drawable.edit_clear, 0);
                }
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }
}

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}
class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val artist: TextView = itemView.findViewById(R.id.foundArtistName)
    private val trackName: TextView = itemView.findViewById(R.id.foundTrackName)
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
    private val trackTime: TextView = itemView.findViewById(R.id.foundTrackTime)

    fun bind(model: Track) {
        artist.setText(model.artistName)
        trackName.setText(model.trackName)
        trackTime.setText(model.trackTime)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder)
            .into(albumCover)
    }
}
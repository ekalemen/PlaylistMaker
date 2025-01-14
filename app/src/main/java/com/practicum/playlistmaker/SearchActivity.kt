package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

const val EXTRA_TRACK_INFO = "EXTRA_TRACK_INFO"
class SearchActivity : AppCompatActivity() {
    var inputText: String = AMOUNT_DEF
    private lateinit var searchEditText: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderUpdateButton: Button
    private lateinit var searchHistoryHeader: TextView
    private lateinit var searchHistoryClearButton: Button
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var recyclerView: RecyclerView

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private var foundTracks = ArrayList<Track>()

    private val searchTrackAdapter = TrackAdapter { playTrack(it) }
    private var historyTrackAdapter = TrackAdapter { playTrack(it) }

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

        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        searchHistory.getSavedHistory()

        recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        recyclerView.adapter = searchTrackAdapter

        searchEditText = findViewById<EditText>(R.id.searchEditText)
        searchEditText.setText(inputText)

        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderUpdateButton = findViewById(R.id.placeholderUpdateButton)

        searchHistoryHeader = findViewById(R.id.search_history_header)
        searchHistoryClearButton = findViewById(R.id.clear_history_button)

        val buttonSearchBack = findViewById<ImageView>(R.id.srch_button_back)

        buttonSearchBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        searchHistoryClearButton.setOnClickListener {
            searchHistory.clearTrackHistory()
            historyTrackAdapter.updateTracksList(searchHistory.getHistoryTracks())
            setHistoryVisibility(false)
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchHistory.getHistoryTracks().isNotEmpty()) {
                setHistoryVisibility(hasFocus)
            }
        }

        placeholderUpdateButton.setOnClickListener {
            search()
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
            foundTracks.clear()
            searchTrackAdapter.updateTracksList(foundTracks)
            placeholderImage.visibility = View.GONE
            placeholderUpdateButton.visibility = View.GONE
            showPlaceHolderText("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setHistoryVisibility(searchEditText.hasFocus() && s?.isEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
                    foundTracks.clear()
                    searchTrackAdapter.updateTracksList(foundTracks)
                    placeholderImage.visibility = View.GONE
                    placeholderUpdateButton.visibility = View.GONE
                    showPlaceHolderText("")
                } else {
                    searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, R.drawable.edit_clear, 0);
                }
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }

    override fun onPause() {
        searchHistory.saveTrackHistory()
        super.onPause()
    }

    private fun search() {
        if (searchEditText.text.isNotEmpty()) {
            iTunesService.search(searchEditText.text.toString()).enqueue(object : Callback<ITunesResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                        if (response.code() == 200) {
                            foundTracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                placeholderImage.visibility = View.GONE
                                placeholderUpdateButton.visibility = View.GONE
                                foundTracks.addAll(response.body()?.results!!)
                                searchTrackAdapter.updateTracksList(foundTracks)
                            }
                            if (foundTracks.isEmpty()) {
                                placeholderImage.setImageResource(R.drawable.ic_search_error)
                                placeholderImage.visibility = View.VISIBLE
                                placeholderUpdateButton.visibility = View.GONE
                                showPlaceHolderText(getString(R.string.found_nothing))
                            } else {
                                placeholderImage.visibility = View.GONE
                                placeholderUpdateButton.visibility = View.GONE
                                showPlaceHolderText("")
                            }
                        } else {
                            placeholderImage.setImageResource(R.drawable.ic_server_error)
                            placeholderImage.visibility = View.VISIBLE
                            placeholderUpdateButton.visibility = View.VISIBLE
                            showPlaceHolderText(getString(R.string.something_went_wrong))
                        }
                    }

                    override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                        placeholderImage.setImageResource(R.drawable.ic_server_error)
                        placeholderImage.visibility = View.VISIBLE
                        placeholderUpdateButton.visibility = View.VISIBLE
                        showPlaceHolderText(getString(R.string.something_went_wrong))
                    }
                })
        }
    }

    fun playTrack(track: Track) {
        searchHistory.addTrackToHistory(track)
        val gson = Gson()
        val trackStr = gson.toJson(track)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK_INFO,trackStr)
        startActivity(intent)
    }

    fun setHistoryVisibility(isSearchFieldEmpty: Boolean) {
        if (isSearchFieldEmpty) {
            searchHistoryHeader.visibility = View.VISIBLE
            searchHistoryClearButton.visibility = View.VISIBLE
            historyTrackAdapter.updateTracksList(searchHistory.getHistoryTracks())
            recyclerView.adapter = historyTrackAdapter
        } else {
            searchHistoryHeader.visibility = View.GONE
            searchHistoryClearButton.visibility = View.GONE
            historyTrackAdapter.updateTracksList(mutableListOf<Track>())
            recyclerView.adapter = searchTrackAdapter
        }
    }

    private fun showPlaceHolderText(text: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            foundTracks.clear()
            searchTrackAdapter.updateTracksList(foundTracks)
            placeholderMessage.text = text
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }
}

class TrackAdapter(
    private val onItemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder> () {
    private var tracks = mutableListOf<Track>()

    fun updateTracksList(newTracks: MutableList<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
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
class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val artist: TextView = itemView.findViewById(R.id.foundArtistName)
    private val trackName: TextView = itemView.findViewById(R.id.foundTrackName)
    private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
    private val trackTime: TextView = itemView.findViewById(R.id.foundTrackTime)

    fun bind(model: Track) {
        artist.setText(model.artistName)
        trackName.setText(model.trackName)
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        val formattedTime = dateFormat.format(model.trackTimeMillis)
        trackTime.text = formattedTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder)
            .into(albumCover)
    }
}
package com.practicum.playlistmaker.ui.search_screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.SearchTracksResult
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.main_screen.MainActivity
import com.practicum.playlistmaker.ui.player_screen.PlayerActivity

const val EXTRA_TRACK_INFO = "EXTRA_TRACK_INFO"
class SearchActivity : AppCompatActivity() {
    var inputText: String = AMOUNT_DEF
    private lateinit var binding: ActivitySearchBinding

    private var foundTracks = ArrayList<Track>()

    private val searchTrackAdapter = TrackAdapter { playTrack(it) }
    private var historyTrackAdapter = TrackAdapter { playTrack(it) }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private lateinit var searchTrackInteractor: TracksInteractor
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    companion object {
        private const val TEXT_AMOUNT = "TEXT_AMOUNT"
        private const val AMOUNT_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchTrackInteractor = Creator.provideTracksInteractor()
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)
        searchHistoryInteractor.getSavedHistory()

        binding.tracksRecyclerView.adapter = searchTrackAdapter
        binding.searchEditText.setText(inputText)

        binding.srchButtonBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearTrackHistory()
            historyTrackAdapter.updateTracksList(searchHistoryInteractor.getHistoryTracks())
            setHistoryVisibility(false)
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchHistoryInteractor.getHistoryTracks().isNotEmpty()) {
                setHistoryVisibility(hasFocus)
            }
        }

        binding.placeholderUpdateButton.setOnClickListener {
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

        binding.searchEditText.onRightDrawableClicked {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            it.text.clear()
            foundTracks.clear()
            searchTrackAdapter.updateTracksList(foundTracks)
            binding.placeholderImage.visibility = View.GONE
            binding.placeholderUpdateButton.visibility = View.GONE
            showPlaceHolderText("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                setHistoryVisibility(binding.searchEditText.hasFocus() && s?.isEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
                    foundTracks.clear()
                    searchTrackAdapter.updateTracksList(foundTracks)
                    binding.placeholderImage.visibility = View.GONE
                    binding.placeholderUpdateButton.visibility = View.GONE
                    showPlaceHolderText("")
                } else {
                    binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.search_edit_view_icon, 0,
                        R.drawable.edit_clear, 0);
                }
            }
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)
    }

    override fun onPause() {
        searchHistoryInteractor.saveTrackHistory()
        super.onPause()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private val searchRunnable = Runnable { search() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search() {
        if (binding.searchEditText.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            searchTrackInteractor.searchTracks(
                binding.searchEditText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(searchRes: SearchTracksResult) {
                        runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            if (searchRes.resultCode == 200) {
                                foundTracks.clear()
                                if (searchRes.tracks.isNotEmpty() == true) {
                                    binding.placeholderImage.visibility = View.GONE
                                    binding.placeholderUpdateButton.visibility = View.GONE
                                    foundTracks.addAll(searchRes.tracks)
                                    searchTrackAdapter.updateTracksList(foundTracks)
                                    searchTrackAdapter.notifyDataSetChanged()
                                }
                                if (foundTracks.isEmpty()) {
                                    binding.placeholderImage.setImageResource(R.drawable.ic_search_error)
                                    binding.placeholderImage.visibility = View.VISIBLE
                                    binding.placeholderUpdateButton.visibility = View.GONE
                                    showPlaceHolderText(getString(R.string.found_nothing))
                                } else {
                                    binding.placeholderImage.visibility = View.GONE
                                    binding.placeholderUpdateButton.visibility = View.GONE
                                    showPlaceHolderText("")
                                }
                            } else {
                                binding.placeholderImage.setImageResource(R.drawable.ic_server_error)
                                binding.placeholderImage.visibility = View.VISIBLE
                                binding.placeholderUpdateButton.visibility = View.VISIBLE
                                showPlaceHolderText(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                }
            )
        }
    }

    fun playTrack(track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
        val gson = Gson()
        val trackStr = gson.toJson(track)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRA_TRACK_INFO,trackStr)
        startActivity(intent)
    }

    fun setHistoryVisibility(isSearchFieldEmpty: Boolean) {
        if (isSearchFieldEmpty) {
            binding.searchHistoryHeader.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.VISIBLE
            historyTrackAdapter.updateTracksList(searchHistoryInteractor.getHistoryTracks())
            binding.tracksRecyclerView.adapter = historyTrackAdapter
        } else {
            binding.searchHistoryHeader.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            historyTrackAdapter.updateTracksList(mutableListOf<Track>())
            binding.tracksRecyclerView.adapter = searchTrackAdapter
        }
    }

    private fun showPlaceHolderText(text: String) {
        if (text.isNotEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
            foundTracks.clear()
            searchTrackAdapter.updateTracksList(foundTracks)
            binding.placeholderMessage.text = text
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
    }
}

package com.practicum.playlistmaker.search.ui.activity

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
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity
import com.practicum.playlistmaker.search.ui.viewmodel.SearchTracksScreenState
import com.practicum.playlistmaker.search.ui.viewmodel.SearchViewModel

const val EXTRA_TRACK_INFO = "EXTRA_TRACK_INFO"
class SearchActivity : AppCompatActivity() {
    var inputText: String = AMOUNT_DEF
    private lateinit var binding: ActivitySearchBinding

    private var foundTracks = listOf<Track>()
    private var historyTracks =  listOf<Track>()

    private val searchTrackAdapter = TrackAdapter { playTrack(it) }
    private var historyTrackAdapter = TrackAdapter { playTrack(it) }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private lateinit var viewModel: SearchViewModel

    companion object {
        private const val TEXT_AMOUNT = "TEXT_AMOUNT"
        private const val AMOUNT_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
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

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(
                Creator.provideTracksInteractor(),
                Creator.provideSearchHistoryInteractor(this)
            )
        )[SearchViewModel::class.java]

        viewModel.historyTracks.observe(this) { historyTracks = it }
        viewModel.searchTracks.observe(this) { foundTracks = it }
        viewModel.searchTracksScreenState.observe(this) { renderSearchScreen(it) }

        viewModel.getSavedHistory()

        binding.srchButtonBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        binding.tracksRecyclerView.adapter = searchTrackAdapter
        binding.searchEditText.setText(inputText)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracks(binding.searchEditText.text.toString())
                true
            }
            false
        }

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.searchEditOnFocusChange(hasFocus)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.onClearTrackHistory()
        }


        binding.placeholderUpdateButton.setOnClickListener {
            viewModel.searchTracks(binding.searchEditText.text.toString())
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
            viewModel.onClearSearchTrackText()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTrackTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()) {
                    binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search_edit_view_icon, 0, 0, 0);
                } else {
                    binding.searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.search_edit_view_icon, 0,
                        R.drawable.edit_clear, 0);
                }
            }
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderSearchScreen(state: SearchTracksScreenState) {
        when (state) {
            SearchTracksScreenState.STATE_LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderUpdateButton.visibility = View.GONE
                binding.searchHistoryHeader.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.GONE
                binding.tracksRecyclerView.visibility = View.GONE
            }
            SearchTracksScreenState.STATE_DISPLAY_SEARCH_RESULT -> {
                binding.progressBar.visibility = View.GONE
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderUpdateButton.visibility = View.GONE
                binding.searchHistoryHeader.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.GONE
                searchTrackAdapter.updateTracksList(foundTracks.toMutableList())
                searchTrackAdapter.notifyDataSetChanged()
                binding.tracksRecyclerView.adapter = searchTrackAdapter
                binding.tracksRecyclerView.visibility = View.VISIBLE
            }
            SearchTracksScreenState.STATE_DISPLAY_HISTORY -> {
                binding.progressBar.visibility = View.GONE
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderUpdateButton.visibility = View.GONE
                binding.placeholderMessage.text = ""
                binding.searchHistoryHeader.visibility = View.VISIBLE
                binding.clearHistoryButton.visibility = View.VISIBLE
                historyTrackAdapter.updateTracksList(historyTracks.toMutableList())
                historyTrackAdapter.notifyDataSetChanged()
                binding.tracksRecyclerView.visibility = View.VISIBLE
                binding.tracksRecyclerView.adapter = historyTrackAdapter
            }
            SearchTracksScreenState.STATE_ERROR ->{
                binding.searchHistoryHeader.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.placeholderImage.setImageResource(R.drawable.ic_server_error)
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderUpdateButton.visibility = View.VISIBLE
                binding.placeholderMessage.text = getString(R.string.something_went_wrong)
                binding.tracksRecyclerView.visibility = View.GONE
            }
            SearchTracksScreenState.STATE_EMPTY_RESULT -> {
                binding.searchHistoryHeader.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.placeholderImage.setImageResource(R.drawable.ic_search_error)
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderUpdateButton.visibility = View.GONE
                binding.placeholderMessage.text = getString(R.string.found_nothing)
                binding.tracksRecyclerView.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        viewModel.saveHistory()
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

    fun playTrack(track: Track) {
        if(clickDebounce()) {
            viewModel.addTrackToHistory(track)
            val gson = Gson()
            val trackStr = gson.toJson(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(EXTRA_TRACK_INFO, trackStr)
            startActivity(intent)
        }
    }
}

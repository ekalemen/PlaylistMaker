package com.practicum.playlistmaker.search.ui.viewmodel

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.SearchTracksResult
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.SearchActivity

enum class SearchTracksScreenState {
    STATE_LOADING,
    STATE_DISPLAY_SEARCH_RESULT,
    STATE_DISPLAY_HISTORY,
    STATE_ERROR,
    STATE_EMPTY_RESULT
}
class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
): ViewModel() {
    companion object {
        fun getViewModelFactory(
            searchTrackInteractor: TracksInteractor,
            searchHistoryInteractor: SearchHistoryInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(searchTrackInteractor, searchHistoryInteractor)
            }
        }
        private val SEARCH_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private val _searchTracksScreenState = MutableLiveData<SearchTracksScreenState>()
    val searchTracksScreenState: LiveData<SearchTracksScreenState> = _searchTracksScreenState

    private val _searchTracks = MutableLiveData<List<Track>>()
    val searchTracks: LiveData<List<Track>> = _searchTracks

    private val _historyTracks = MutableLiveData<List<Track>>()
    val historyTracks: LiveData<List<Track>> = _historyTracks

    private val handler = Handler(Looper.getMainLooper())

    fun getSavedHistory() {
        _historyTracks.postValue(searchHistoryInteractor.getSavedHistory())
    }

    fun saveHistory() {
        searchHistoryInteractor.saveTrackHistory()
    }

    fun addTrackToHistory (track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
    }

    fun onClearTrackHistory() {
        searchHistoryInteractor.clearTrackHistory()
        _historyTracks.postValue(listOf())
        _searchTracks.postValue(listOf())
        _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_DISPLAY_SEARCH_RESULT)
    }

    fun searchEditOnFocusChange(focus: Boolean) {
        if (focus) {
            _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_DISPLAY_HISTORY)
        }
    }

    fun onSearchTrackTextChanged(searchText: String) {
        if (searchText.isEmpty()) {
            _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_DISPLAY_HISTORY)
            _historyTracks.postValue(searchHistoryInteractor.getHistoryTracks())
        } else
            searchDebounce(searchText)
    }

    fun onClearSearchTrackText() {
        _searchTracks.postValue(listOf())
        _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_DISPLAY_HISTORY)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_TOKEN)
    }

    fun searchDebounce(searchText: String) {
        val searchRunnable = Runnable { searchTracks(searchText) }
        handler.removeCallbacksAndMessages(SEARCH_TOKEN)
        handler.postDelayed(searchRunnable, SEARCH_TOKEN, SearchViewModel.SEARCH_DEBOUNCE_DELAY)

    }
    fun searchTracks(trackText: String) {
        if (trackText.isNotEmpty()) {
            _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_LOADING)
            tracksInteractor.searchTracks(
                trackText,
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(searchRes: SearchTracksResult) {
                            if (searchRes.resultCode == 200) {
                                if (searchRes.tracks.isNotEmpty() == true) {
                                    _searchTracks.postValue(searchRes.tracks)
                                    _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_DISPLAY_SEARCH_RESULT)
                                } else {
                                    _searchTracks.postValue(listOf())
                                    _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_EMPTY_RESULT)
                                }
                            } else {
                                _searchTracksScreenState.postValue(SearchTracksScreenState.STATE_ERROR)
                            }

                    }
                }
            )
        }
    }
}
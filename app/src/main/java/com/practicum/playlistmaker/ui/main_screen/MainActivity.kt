package com.practicum.playlistmaker.ui.main_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.media_screen.MediaActivity
import com.practicum.playlistmaker.ui.settings_screen.SettingsActivity
import com.practicum.playlistmaker.ui.search_screen.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMainSearch = findViewById<Button>(R.id.main_butt_search)
        val buttonMainMedia = findViewById<Button>(R.id.main_butt_media)
        val buttonMainSettings = findViewById<Button>(R.id.main_butt_settings)

        buttonMainSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonMainMedia.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonMainSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
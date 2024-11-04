package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMainSearch = findViewById<Button>(R.id.main_butt_search)
        val buttonMainMedia = findViewById<Button>(R.id.main_butt_media)
        val buttonMainSettings = findViewById<Button>(R.id.main_butt_settings)

        /*val buttonOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                when(v?.id) {
                    R.id.main_butt_search -> {
                        Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск", Toast.LENGTH_SHORT).show()
                    }
                    R.id.main_butt_media -> {
                        Toast.makeText(this@MainActivity, "Нажали на кнопку Медиа", Toast.LENGTH_SHORT).show()
                    }
                    R.id.main_butt_settings -> {
                        Toast.makeText(this@MainActivity, "Нажали на кнопку Настройки", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }*/


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
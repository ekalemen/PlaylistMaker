package com.practicum.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText

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

        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        searchEditText.setText(inputText)

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
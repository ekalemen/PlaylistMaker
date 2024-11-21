package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonSettingsBack = findViewById<ImageView>(R.id.settings_button_back)

        buttonSettingsBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        val shareAppFrame = findViewById<FrameLayout>(R.id.settings_share_app_frame)

        shareAppFrame.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_share_url))
            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent,"Share link"))
            //Toast.makeText(this, "Нажали на фрейм Поделиться приложением", Toast.LENGTH_SHORT).show()
        }

        val writeToSupportFrame = findViewById<FrameLayout>(R.id.settings_write_to_support_frame)

        writeToSupportFrame.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.support_email)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_mail_body))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.support_mail_subject))
            startActivity(supportIntent)
            //Toast.makeText(this, "Нажали на фрейм Write to support", Toast.LENGTH_SHORT).show()
        }

        val userAgreementFrame = findViewById<FrameLayout>(R.id.settings_user_agreement_frame)

        userAgreementFrame.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.setData(Uri.parse(resources.getString(R.string.support_user_agreement_url)))
            startActivity(userAgreementIntent)
            //Toast.makeText(this, "Нажали на фрейм User Agreement", Toast.LENGTH_SHORT).show()
        }
    }
}
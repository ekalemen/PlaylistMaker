package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType
import com.practicum.playlistmaker.main.ui.MainActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcherInteractor: ThemeSwitcherInteractor
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeSwitcherInteractor = Creator.provideThemeSwitcherInteractor(this)

        binding.settingsButtonBack.setOnClickListener {
            val setBackToMainIntent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(setBackToMainIntent)
        }

        binding.settingsShareAppFrame.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_share_url))
            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent,"Share link"))
        }

        binding.settingsWriteToSupportFrame.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.support_email)))
            supportIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_mail_body))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.support_mail_subject))
            startActivity(supportIntent)
        }

        binding.settingsUserAgreementFrame.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.setData(Uri.parse(resources.getString(R.string.support_user_agreement_url)))
            startActivity(userAgreementIntent)
        }

        binding.themeSwitcher.isChecked = when(themeSwitcherInteractor.getSavedTheme()?.theme) {
            ThemeType.DARK -> true
            ThemeType.LIGHT -> false
            null -> false
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (binding.themeSwitcher.isChecked)
                themeSwitcherInteractor.switchTheme(PlayListTheme(ThemeType.DARK))
            else
                themeSwitcherInteractor.switchTheme(PlayListTheme(ThemeType.LIGHT))
        }
    }
}
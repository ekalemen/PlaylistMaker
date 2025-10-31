package com.practicum.playlistmaker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.main.ui.MainActivity
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType
import com.practicum.playlistmaker.settings.ui.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                Creator.provideSharingInteractor(this),
                Creator.provideThemeSwitcherInteractor(this)
            )
        )[SettingsViewModel::class.java]

        binding.settingsButtonBack.setOnClickListener {
            finish()
        }

        binding.settingsShareAppFrame.setOnClickListener {
            viewModel.shareApp()
        }

        binding.settingsWriteToSupportFrame.setOnClickListener {
            viewModel.sendEmailToSupport()
        }

        binding.settingsUserAgreementFrame.setOnClickListener {
            viewModel.openAgreement()
        }

        viewModel.getSavedTheme()
        viewModel.currentTheme.observe(this) { currentTheme ->
            binding.themeSwitcher.isChecked = when(currentTheme.theme) {
                ThemeType.DARK -> true
                ThemeType.LIGHT -> false
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (binding.themeSwitcher.isChecked)
                viewModel.changeTheme(PlayListTheme(ThemeType.DARK))
            else
                viewModel.changeTheme(PlayListTheme(ThemeType.LIGHT))
        }
    }
}
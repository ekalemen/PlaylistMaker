package com.practicum.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.models.PlayListTheme
import com.practicum.playlistmaker.settings.domain.models.ThemeType
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeSwitcherInteractor
) : ViewModel() {

    private val _currentTheme = MutableLiveData<PlayListTheme>()
    val currentTheme: LiveData<PlayListTheme> = _currentTheme
    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            themeInteractor: ThemeSwitcherInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(sharingInteractor, themeInteractor)
            }
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendEmailToSupport() {
        sharingInteractor.sendEmail()
    }

    fun openAgreement() {
        sharingInteractor.openTerms()
    }

    fun changeTheme(theme: PlayListTheme) {
        themeInteractor.switchTheme(theme)
        _currentTheme.postValue(theme)
    }

    fun getSavedTheme() {
        if(themeInteractor.getSavedTheme() == null)
            _currentTheme.postValue(PlayListTheme(ThemeType.LIGHT))
        else
            _currentTheme.postValue(themeInteractor.getSavedTheme())
    }
}
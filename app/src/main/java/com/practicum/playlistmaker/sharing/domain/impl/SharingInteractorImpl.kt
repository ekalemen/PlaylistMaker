package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {
    override fun shareApp() {
        externalNavigator.sendLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun sendEmail() {
        externalNavigator.sendEmail(getEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getShareAppLink()
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermsLink()
    }

    private fun getEmailData(): EmailData {
        return externalNavigator.getEmailData()
    }
}
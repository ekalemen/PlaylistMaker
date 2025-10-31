package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun sendLink(link:String)
    fun sendEmail(email: EmailData)
    fun openLink(link:String)
    fun getTermsLink():String
    fun getShareAppLink(): String
    fun getEmailData(): EmailData
}
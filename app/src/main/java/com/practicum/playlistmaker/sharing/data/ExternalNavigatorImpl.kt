package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun sendLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.setType("text/plain")
        val choosenIntent = Intent.createChooser(shareIntent,"Share link")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(choosenIntent)
        
    }

    override fun sendEmail(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        supportIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_mail_body))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_mail_subject))
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }

    override fun openLink(link: String) {
        val userAgreementIntent = Intent(Intent.ACTION_VIEW)
        userAgreementIntent.setData(Uri.parse(context.getString(R.string.support_user_agreement_url)))
        userAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(userAgreementIntent)
    }
    override fun getTermsLink():String {
        return context.getString(R.string.support_user_agreement_url)
    }
    override fun getShareAppLink(): String {
        return context.getString(R.string.support_share_url)
    }
    override fun getEmailData(): EmailData {
        return EmailData(
            emailAddress = context.getString(R.string.support_email),
            emailSubject = context.getString(R.string.support_mail_subject),
            emailText = context.getString(R.string.support_mail_body)
        )
    }
}
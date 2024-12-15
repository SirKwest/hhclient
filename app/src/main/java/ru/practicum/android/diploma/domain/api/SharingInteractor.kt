package ru.practicum.android.diploma.domain.api

interface SharingInteractor {
    fun shareText(text: String)
    fun openEmail(emailAddress: String)
    fun dial(number: String)
}

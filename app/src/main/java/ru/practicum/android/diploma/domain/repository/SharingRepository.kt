package ru.practicum.android.diploma.domain.repository

interface SharingRepository {
    fun shareText(text: String)
    fun openEmail(emailAddress: String)
    fun dial(number: String)
}

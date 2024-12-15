package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.repository.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
) : SharingInteractor {
    override fun shareText(text: String) {
        sharingRepository.shareText(text)
    }

    override fun openEmail(emailAddress: String) {
        sharingRepository.openEmail(emailAddress)
    }

    override fun dial(number: String) {
        sharingRepository.dial(number)
    }
}

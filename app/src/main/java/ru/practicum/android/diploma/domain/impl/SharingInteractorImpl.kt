package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.repository.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
) : SharingInteractor {

    override fun shareText(text: String) {
        sharingRepository.shareText(text)
    }
}
